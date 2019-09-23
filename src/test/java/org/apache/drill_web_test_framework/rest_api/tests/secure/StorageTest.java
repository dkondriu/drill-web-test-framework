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
package org.apache.drill_web_test_framework.rest_api.tests.secure;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class StorageTest extends BaseRestTest {
  @Test
  public void nonAdminStoragePage() {
    given()
        .filter(nonAdminSessionFilter)
        .get("/storage.json")
        .then()
        .statusCode(500)
        .body(containsString("User not authorized."));
  }

  @Test
  public void adminStoragePage() {
    //Get the response
    Response response = given()
        .filter(adminSessionFilter)
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
            .pathParam("pluginName", pluginName)
            .filter(adminSessionFilter)
            .when()
            .get("/storage/{pluginName}.json")
            .then()
            .statusCode(200)
            .body("config.enabled", is(true))
            .body("config.type", equalTo("file"))
            .body("config.connection", containsString(":///"));
      } else if (pluginName.equalsIgnoreCase("kudu") || pluginName.equalsIgnoreCase("mongo") || pluginName.equalsIgnoreCase("s3")) {
        given()
            .pathParam("pluginName", pluginName)
            .filter(adminSessionFilter)
            .when()
            .get("/storage/{pluginName}.json")
            .then()
            .statusCode(200)
            .body("config.enabled", is(false));
      }
    }
  }

  @Test
  public void nonAdminAddPlugin() {
    String newPlugin = "{" +
        "  \"name\":\"testPlugin1\"," +
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
        .filter(nonAdminSessionFilter)
        .body(newPlugin)
        .with()
        .contentType("application/json")
        .when()
        .post("/storage/testPlugin1.json")
        .then()
        .statusCode(500)
        .body(containsString("User not authorized."));
  }

  @Test
  public void adminAddPlugin() {
    String newPlugin = "{" +
        "  \"name\":\"testPlugin1\"," +
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
        .filter(adminSessionFilter)
        .body(newPlugin)
        .with()
        .contentType("application/json")
        .when()
        .post("/storage/testPlugin1.json")
        .then()
        .statusCode(200)
        .body(containsString("Success"));
  }

  @Test(dependsOnMethods = {"adminAddPlugin"})
  public void checkTestPlugin() {
    given()
        .filter(adminSessionFilter)
        .when()
        .get("/storage/testPlugin1.json")
        .then()
        .statusCode(200)
        .body("name", equalTo("testPlugin1"))
        .body("config.enabled", equalTo(false))
        .body("config.workspaces.drilltestdirp1.location", equalTo("/drill/testdata/p1tests"));
    // drillTestDirP1 changed to drilltestdirp1 after plugin created
    //.body("config.workspaces.drillTestDirP1.location", equalTo("/drill/testdata/p1tests"));
  }

  @Test(dependsOnMethods = {"checkTestPlugin"})
  public void adminUpdateTestPlugin() {
    String updatePlugin = "{" +
        "  \"name\": \"testPlugin1\"," +
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
        .filter(adminSessionFilter)
        .body(updatePlugin)
        .with()
        .contentType("application/json")
        .when()
        .post("/storage/testPlugin1.json")
        .then()
        .statusCode(200)
        .body(containsString("Success"));
  }

  @Test(dependsOnMethods = {"adminUpdateTestPlugin"})
  public void nonAdminDeletePlugin() {
    given()
        .filter(nonAdminSessionFilter)
        .when()
        .delete("/storage/testPlugin1.json")
        .then()
        .statusCode(500)
        .body(containsString("User not authorized."));
  }

  @Test(dependsOnMethods = {"adminUpdateTestPlugin"})
  public void adminDeletePlugin() {
    given()
        .filter(adminSessionFilter)
        .when()
        .delete("/storage/testPlugin1.json")
        .then()
        .statusCode(200)
        .body(containsString("Success"));
  }
}
