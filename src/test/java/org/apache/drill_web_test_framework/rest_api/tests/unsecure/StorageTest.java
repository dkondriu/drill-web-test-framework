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
package org.apache.drill_web_test_framework.rest_api.tests.unsecure;

import io.restassured.response.Response;
import org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

public class StorageTest extends BaseRestTest {
  private String queryId_1 = null;

  @BeforeClass
  public void removePluginIfExists() {
    given()
        .filter(sessionFilter)
        .when()
        .delete("/storage/testplugin.json")
        .then()
        .statusCode(200);
  }

  @Test
  public void testStoragePage() {
    given()
        .filter(sessionFilter)
        .when()
        .get("/storage.json")
        .then()
        .statusCode(200)
        .body("name", hasItems("cp", "dfs", "hive", "kafka", "kudu", "mongo", "s3"));
  }

  @Test
  public void testStorageStoragePlugins() {
    //Get the response
    Response response = given()
        .filter(sessionFilter)
        .when()
        .get("/storage.json")
        .then()
        .extract()
        .response();
    //Extract the plugin names from the response
    List<String> pluginNames = response.jsonPath().getList("name");
    //Iterate through the pluginNames
    int i;
    for (i = 0; i <= pluginNames.size(); i++) {
      String pluginName = response.jsonPath().param("i", i).getString("name[i]");
      //Skip plugins with null names
      if (pluginName == null) {
        continue;
      } else if (pluginName.equalsIgnoreCase("cp") || pluginName.equalsIgnoreCase("dfs")) {
        given()
            .filter(sessionFilter)
            .pathParam("pluginName", pluginName)
            .when()
            .get("/storage/{pluginName}.json")
            .then()
            .statusCode(200)
            .body("config.enabled", is(true))
            .body("config.type", equalTo("file"))
            .body("config.connection", containsString(":///"));
      } else if (pluginName.equalsIgnoreCase("kudu") || pluginName.equalsIgnoreCase("mongo") || pluginName.equalsIgnoreCase("s3")) {
        given()
            .filter(sessionFilter)
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
        "  \"name\":\"testplugin\"," +
        "  \"config\": {" +
        "    \"type\": \"file\"," +
        "    \"enabled\": \"false\"," +
        "    \"connection\": \"maprfs:///\"," +
        "    \"workspaces\": {" +
        "      \"drilltestdirp1\": {" +
        "        \"location\": \"/drill/testdata/p1tests\"," +
        "        \"writable\": \"false\"," +
        "        \"defaultInputFormat\": \"parquet\"" +
        "      }" +
        "    }" +
        "  }" +
        "}";
    given()
        .filter(sessionFilter)
        .body(newPlugin)
        .with()
        .contentType("application/json")
        .when()
        .post("/storage/testPlugin.json")
        .then()
        .statusCode(200)
        .body(containsString("Success"));
  }

  @Test(dependsOnMethods = {"addNewPlugin"})
  public void getTestPlugin() {
    given()
        .filter(sessionFilter)
        .when()
        .get("/storage/testplugin.json")
        .then()
        .statusCode(200)
        .body("name", equalTo("testplugin"))
        .body("config.enabled", equalTo(false))
        .body("config.workspaces.drilltestdirp1.location", equalTo("/drill/testdata/p1tests"));
  }

  //Disable due to DRILL-6306-Should not be able to run queries against disabled storage plugins
  //@Test(dependsOnMethods = {"updateTestPlugin"})
  public void queryTestPlugin() {
    String query = "{\"queryType\":\"SQL\",\"query\":\"SELECT * FROM testplugin.drilltestdirp1.voter\"}";
    given()
        .filter(sessionFilter)
        .body(query)
        .with()
        .contentType("application/json")
        .when()
        .post("/query.json")
        .then()
        .statusCode(500)
        .body("errorMessage", containsString("Table 'testPlugin.drillTestDirP1.voter' not found"));
  }

  @Test(dependsOnMethods = {"addNewPlugin", "getTestPlugin"})//Use this dependency for now due to DRILL-6306
  public void updateTestPlugin() {
    String updatePlugin = "{" +
        "  \"name\": \"testplugin\"," +
        "  \"config\": {" +
        "    \"type\": \"file\"," +
        "    \"enabled\": \"true\"," +
        "    \"connection\": \"maprfs:///\"," +
        "    \"workspaces\": {" +
        "      \"drilltestdirp1\": {" +
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
        .filter(sessionFilter)
        .body(updatePlugin)
        .with()
        .contentType("application/json")
        .when()
        .post("/storage/testplugin.json")
        .then()
        .statusCode(200)
        .body(containsString("Success"));
  }

  @Test(dependsOnMethods = {"updateTestPlugin"})
  public void checkUpdatedTestPlugin() {
    given()
        .filter(sessionFilter)
        .when()
        .get("/storage/testplugin.json")
        .then()
        .statusCode(200)
        .body("config.enabled", is(true))
        .root("config.workspaces")
        .body("drilltestdirp1.writable", is(true))
        .body("testschema.location", equalTo("/drill/testdata"));
  }

  @Test(dependsOnMethods = {"updateTestPlugin"})
  public void queryUpdatedTestPlugin() {
    RestBaseSteps.runQuery("DROP TABLE IF EXISTS `testplugin.drilltestdirp1`.voter", sessionFilter);
    RestBaseSteps.runQuery("CREATE TABLE `testplugin.drilltestdirp1`.voter AS SELECT * FROM cp.`employee.json` LIMIT 2", sessionFilter);
    String query = "{" +
        "   \"queryType\": \"SQL\"," +
        "   \"query\": \"select count(*) as total_cnt from `testplugin.drilltestdirp1`.voter\"" +
        "}";
    given()
        .filter(sessionFilter)
        .body(query)
        .with()
        .contentType("application/json")
        .when()
        .post("/query.json")
        .then()
        .statusCode(200)
        .body("columns[0]", equalTo("total_cnt"))
        .body("rows.total_cnt[0]", equalTo("2"));
    //Get the response
    Response response = get("/profiles.json").then().extract().response();
    //Extract the entries for finishedQueries from the response
    List<String> jsonResponse = response.jsonPath().getList("finishedQueries");
    //Iterate through the repsonse to find the matching finished query
    int i;
    for (i = 0; i <= jsonResponse.size(); i++) {
      if (query.equalsIgnoreCase(response.jsonPath().param("i", i).getString("finishedQueries.query[i]"))) {
        break;
      }
      break;
    }
    queryId_1 = response.jsonPath().param("i", i).getString("finishedQueries.queryId[i]");
  }

  @Test(dependsOnMethods = {"queryUpdatedTestPlugin"})
  public void verifyTestQueryProfileInfo() {

    given()
        .filter(sessionFilter)
        .pathParam("queryID", queryId_1)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state", equalTo(2))
        .body("query", equalTo("select count(*) as total_cnt from `testplugin.drilltestdirp1`.voter"))
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
        .filter(sessionFilter)
        .when()
        .get("/storage/testplugin/enable/false")
        .then()
        .statusCode(200)
        .body(containsString("Success"));
  }

  @Test(dependsOnMethods = {"disableTestPlugin"})
  public void verifyDisabledTestPlugin() {
    given()
        .filter(sessionFilter)
        .when()
        .get("/storage/testplugin.json")
        .then()
        .statusCode(200)
        .body("name", equalTo("testplugin"))
        .body("config.enabled", equalTo(false));
  }

  @Test(dependsOnMethods = {"verifyDisabledTestPlugin"})
  public void deleteTestPlugin() {
    given()
        .filter(sessionFilter)
        .when()
        .delete("/storage/testplugin.json")
        .then()
        .statusCode(200)
        .body(containsString("Success"));
  }

  @Test(dependsOnMethods = {"deleteTestPlugin"})
  public void checkDeleteTestPlugin() {
    given()
        .filter(sessionFilter)
        .when()
        .get("/storage/testplugin.json")
        .then()
        .statusCode(200)
        .body("name", equalTo("testplugin"))
        .body(not(contains("workspaces")))
        .body(not(contains("type")))
        .body(not(contains("workspaces")))
        .body(not(contains("enabled")));
  }
}
