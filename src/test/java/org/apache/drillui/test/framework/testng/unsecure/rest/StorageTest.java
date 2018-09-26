/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.drillui.test.framework.testng.unsecure.rest;

import io.restassured.response.Response;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.testng.TestRunner.PriorityWeight.dependsOnMethods;

public class StorageTest extends BaseRestTest {
  @Test
  public void testStoragePage() {
    given()
        .when()
        .get("/storage.json")
        .then()
        .statusCode(200)
        .body("name", hasItems("cp","dfs","hive","kafka","kudu","mongo","s3"));
  }
  @Test
  public void testStorageStoragePlugins() {
    //Get the response
    Response response = get("/storage.json").then().extract().response();
    //Extract the plugin names from the response
    List<String> pluginNames = response.jsonPath().getList("name");
    //Iterate through the pluginNames
    int i;
    for(i = 0; i <= pluginNames.size(); i++) {
      String pluginName = response.jsonPath().param("i", i).getString("name[i]");
      //Skip plugins with null names
      if(pluginName == null) {
        continue;
      }
      else if(pluginName.equalsIgnoreCase("cp") || pluginName.equalsIgnoreCase("dfs")) {
        given()
            .pathParam("pluginName", pluginName)
            .when()
            .get("/storage/{pluginName}.json")
            .then()
            .statusCode(200)
            .body("config.enabled", is(true))
            .body("config.type", equalTo("file"))
            .body("config.connection",containsString(":///"));
      }
      else if(pluginName.equalsIgnoreCase("kudu") || pluginName.equalsIgnoreCase("mongo") || pluginName.equalsIgnoreCase("s3")) {
        given()
            .pathParam("pluginName", pluginName)
            .when()
            .get("/storage/{pluginName}.json")
            .then()
            .statusCode(200)
            .body("config.enabled", is(false));
      }
    }
  }
  @Test
  public void addNewPlugin() {
    String newPlugin = "{" +
        "  \"name\":\"testPlugin\"," +
        "  \"config\": {" +
        "    \"type\": \"file\"," +
        "    \"enabled\": \"false\"," +
        "    \"connection\": \"maprfs:///\"," +
        "    \"workspaces\": {" +
        "      \"drillTestDirP1\": {" +
        "        \"location\": \"/drill/testdata/p1tests\"," +
        "        \"writable\": \"false\"," +
        "        \"defaultInputFormat\": \"parquet\"" +
        "      }" +
        "    }" +
        "  }" +
        "}";
    given()
        .body(newPlugin)
        .with()
        .contentType("application/json")
        .when()
        .post("/storage/testPlugin.json")
        .then()
        .statusCode(200)
        .body(containsString("success"));
  }
  @Test(dependsOnMethods = {"addNewPlugin"})
  public void getTestPlugin() {
    given()
        .when()
        .get("/storage/testPlugin.json")
        .then()
        .statusCode(200)
        .body("name", equalTo("testPlugin"))
        .body("config.enabled", equalTo(false))
        .body("config.workspaces.drilltestdirp1.location", equalTo("/drill/testdata/p1tests"));
        // drillTestDirP1 changed to drilltestdirp1 after plugin created
        //.body("config.workspaces.drillTestDirP1.location", equalTo("/drill/testdata/p1tests"));
  }

  //Disable due to DRILL-6306-Should not be able to run queries against disabled storage plugins
  //@Test(dependsOnMethods = {"updateTestPlugin"})
  public void queryTestPlugin() {
    String query = "{\"queryType\":\"SQL\",\"query\":\"SELECT * FROM cp.`employee.json` LIMIT 2\"}";
    given()
        .body(query)
        .with()
        .contentType("application/json")
        .when()
        .post("/query.json")
        .then()
        .statusCode(500)
        .body("errorMessage",containsString("Table 'testPlugin.drillTestDirP1.voter' not found"));
  }
  //@Test(groups = { "noAuth" },dependsOnMethods = { "queryTestPlugin" })  //use this after DRILL-6306 is fixed.
  @Test(dependsOnMethods = {"addNewPlugin", "getTestPlugin"})//Use this dependency for now due to DRILL-6306
  public void updateTestPlugin() {
    String updatePlugin = "{" +
    "  \"name\": \"testPlugin\"," +
    "  \"config\": {" +
    "    \"type\": \"file\"," +
    "    \"enabled\": \"true\"," +
    "    \"connection\": \"maprfs:///\"," +
    "    \"workspaces\": {" +
    "      \"drillTestDirP1\": {" +
    "        \"location\": \"/drill/testdata/p1tests\"," +
    "        \"writable\": \"true\"," +
    "        \"defaultInputFormat\": \"parquet\"" +
    "      }," +
    "      \"testSchema\": {" +
    "        \"location\": \"/drill/testdata\"," +
    "        \"writable\": \"true\"," +
    "        \"defaultInputFormat\": \"json\"" +
    "      }" +
    "    }" +
    "  }" +
    "}";
    given()
        .body(updatePlugin)
        .with()
        .contentType("application/json")
        .when()
        .post("/storage/testPlugin.json")
        .then()
        .statusCode(200)
        .body(containsString("success"));
  }
  @Test(dependsOnMethods = {"updateTestPlugin"})
  public void checkUpdatedTestPlugin() {
    given()
        .when()
        .get("/storage/testPlugin.json")
        .then()
        .statusCode(200)
        .body("config.enabled", is(true))
        .root("config.workspaces")
        .body("drilltestdirp1.writable", is(true))
        // drillTestDirP1 changed to drilltestdirp1 after plugin updated
        //.body("drillTestDirP1.writable", is(true))
        .body("testschema.location", equalTo("/drill/testdata"));
        // testSchema changed to testschema after plugin updated
        //.body("testSchema.location", equalTo("/drill/testdata"));
  }
  // How to create test tables in the new Storage Plugin???
  //@Test(dependsOnMethods = {"checkUpdatedTestPlugin"})
  public void queryUpdatedTestPlugin() {
    String query = "{" +
        "   \"queryType\": \"SQL\"," +
        //"   \"query\": \"select count(*) as total_cnt from `testPlugin.drillTestDirP1`.voter\"" +
        "   \"query\": \"DROP TABLE IF EXISTS testPlugin.drillTestDirP1.`employee`;" +
        "     CREATE TABLE testPlugin.drillTestDirP1.`employee` AS SELECT * FROM cp.`employee.json` LIMIT 2;\"" +
        "     SELECT * FROM cp.`employee.json` LIMIT 2\"" +
        "}";
    given()
        .body(query)
        .with()
        .contentType("application/json")
        .when()
        .post("/query.json")
        .then()
        .statusCode(200)
        .body("columns[0]", equalTo("employee_id"))
        .body("rows.employee_id[0]",equalTo("1"));
        //.body("columns[0]", equalTo("total_cnt"))
        //.body("rows.total_cnt[0]",equalTo("1000"));
    //Get the response
    Response response = get("/profiles.json").then().extract().response();
    //Extract the entries for finishedQueries from the response
    List<String> jsonResponse = response.jsonPath().getList("finishedQueries");
    //Iterate through the repsonse to find the matching finished query
    int i;
    for(i = 0; i <= jsonResponse.size(); i++) {
      if (query.equalsIgnoreCase(response.jsonPath().param("i", i).getString("finishedQueries.query[i]"))) {
        break;
      }
      break;
    }
    String queryId_1 = response.jsonPath().param("i", i).getString("finishedQueries.queryId[i]");
//  }
//  Dependencies in the tests!!!
//  @Test
//  public void verifyTestQueryProfileInfo() {

    given()
        .pathParam("queryID", queryId_1)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state", equalTo(2))
        .body("query", equalTo("select count(*) as total_cnt from `testPlugin.drillTestDirP1`.voter"))
        .body("plan", containsString("Screen"))
        .body("plan", containsString("Project"))
        .body("plan", containsString("DirectScan"))
        .body("plan", containsString("rowcount = 1.0"))
        .body("plan", containsString("cumulative cost"))
        .body("state", equalTo(2))
        .body("totalFragments", equalTo(1))
        .body("fragmentProfile.minorFragmentProfile.minorFragmentId.flatten()", everyItem(equalTo(0)))
        .body("fragmentProfile.minorFragmentProfile.operatorProfile.peakLocalMemoryAllocated.flatten()", everyItem(greaterThan(0)))
        .body("fragmentProfile.minorFragmentProfile.operatorProfile.operatorId.flatten()", everyItem(greaterThanOrEqualTo(0)))
        .body(containsString("lastUpdate"))
        .body(containsString("user"));
  }
  @Test(dependsOnMethods = {"checkUpdatedTestPlugin"})
  public void disableTestPlugin() {
    given()
        .when()
        .get("/storage/testPlugin/enable/false")
        .then()
        .statusCode(200)
        .body(containsString("success"));
  }
  @Test(dependsOnMethods = {"disableTestPlugin"})
  public void verifyDisabledTestPlugin() {
    given()
        .when()
        .get("/storage/testPlugin.json")
        .then()
        .statusCode(200)
        .body("name", equalTo("testPlugin"))
        .body("config.enabled", equalTo(false));
  }
  @Test(dependsOnMethods = {"verifyDisabledTestPlugin"})
  public void deleteTestPlugin() {
    given()
        .when()
        .delete("/storage/testPlugin.json")
        .then()
        .statusCode(200)
        .body(containsString("success"));
  }
  @Test(dependsOnMethods = {"deleteTestPlugin"})
  public void checkDeleteTestPlugin() {
    given()
        .when()
        .get("/storage/testPlugin.json")
        .then()
        .statusCode(200)
        .body("name", equalTo("testPlugin"))
        .body(not(contains("workspaces")))
        .body(not(contains("type")))
        .body(not(contains("workspaces")))
        .body(not(contains("enabled")));
  }
}
