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
import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;

public class ProfilesTest extends BaseRestTest {
  private static String bigQuery = null;

  @BeforeClass
  public static void getLargeQuery() {
    StringBuffer sqlQuery = new StringBuffer();
    try (Stream<String> stream = Files.lines(new File("queries/big_query.sql").toPath())) {
      stream.forEach(sqlQuery::append);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    bigQuery = sqlQuery.toString();
  }

  @Test
  public void nonAdminProfilesPage() {
    QueryTest queryExecutor = new QueryTest();
    queryExecutor.adminRunQuery();
    queryExecutor.nonAdminRunQuery();
    given()
        .filter(nonAdminSessionFilter)
        .when()
        .get("/profiles.json")
        .then()
        .statusCode(200)
        .body("finishedQueries.user", everyItem(equalTo(PropertiesConst.USER_1_NAME)));
  }

  @Test
  public void adminProfilesPage() {
    QueryTest queryExecutor = new QueryTest();
    queryExecutor.adminRunQuery();
    queryExecutor.nonAdminRunQuery();
    queryExecutor.secondNonAdminRunQuery();
    given()
        .filter(adminSessionFilter)
        .when()
        .get("/profiles.json")
        .then()
        .statusCode(200)
        .body("finishedQueries.user", hasItems(PropertiesConst.USER_1_NAME, PropertiesConst.USER_2_NAME, PropertiesConst.ADMIN_1_NAME));
  }

  @Test
  public void nonAdminCancelSecondNonAdminQuery() throws InterruptedException {
    RestBaseSteps.runQueryInBackground(bigQuery, nonAdminSessionFilter);
    //put 3 seconds sleep time here to make sure query is in running state
    Thread.sleep(3000);
    //Get the response
    Response response = given().filter(adminSessionFilter).get("/profiles.json").then().extract().response();
    //Extract the entries for runningQueries from the response
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    int i;
    for (i = 0; i <= jsonResponse.size(); i++) {
      if (bigQuery.equalsIgnoreCase(response.jsonPath().param("i", i).getString("runningQueries.query[i]"))) {
        break;
      }
      break;
    }
    String queryId = response.jsonPath().param("i", i).getString("runningQueries.queryId[i]");
    //Cancel the query with the queryId
    given()
        .pathParam("queryID", queryId)
        .filter(secondNonAdminSessionFilter)
        .when()
        .get("/profiles/cancel/{queryID}")
        .then()
        .statusCode(500)
        .body(containsString("PERMISSION ERROR: Not authorized to cancel the query '" + queryId + "'"));
    //put 3 seconds sleep time
    Thread.sleep(3000);
    //Check to make sure that query is still running or completed and not cancelled or being cancelled
    given()
        .pathParam("queryID", queryId)
        .filter(adminSessionFilter)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state", anyOf(equalTo(1), equalTo(2)));
  }

  @Test
  public void nonAdminCancelOwnQuery() throws InterruptedException {
    RestBaseSteps.runQueryInBackground(bigQuery, nonAdminSessionFilter);
    //put 3 seconds sleep time here to make sure query is in running state
    Thread.sleep(3000);
    //Get the response
    Response response = given().filter(nonAdminSessionFilter).get("/profiles.json").then().extract().response();
    //Put the running queries into a list
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    int i;
    for (i = 0; i <= jsonResponse.size(); i++) {
      if (bigQuery.equalsIgnoreCase(response.jsonPath().param("i", i).getString("runningQueries.query[i]"))) {
        break;
      }
      break;
    }
    String queryId = response.jsonPath().param("i", i).getString("runningQueries.queryId[i]");
    //Cancel the query with the queryId
    given()
        .pathParam("queryID", queryId)
        .filter(nonAdminSessionFilter)
        .when()
        .get("/profiles/cancel/{queryID}")
        .then()
        .statusCode(200);
    //put 3 seconds sleep time to make sure query is cancelled
    Thread.sleep(3000);
    //Check to make sure that query is cancelled
    given()
        .pathParam("queryID", queryId)
        .filter(nonAdminSessionFilter)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state", equalTo(3));
  }

  @Test
  public void adminCancelSecondNonAdminQuery() throws InterruptedException {
    RestBaseSteps.runQueryInBackground(bigQuery, secondNonAdminSessionFilter);
    //put 3 seconds sleep time here to make sure query is in running state
    Thread.sleep(3000);
    //Get the response
    Response response = given().filter(secondNonAdminSessionFilter).get("/profiles.json").then().extract().response();
    //Put the running queries into a list
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    int i;
    for (i = 0; i <= jsonResponse.size(); i++) {
      if (bigQuery.equalsIgnoreCase(response.jsonPath().param("i", i).getString("runningQueries.query[i]"))) {
        break;
      }
      break;
    }
    String queryId = response.jsonPath().param("i", i).getString("runningQueries.queryId[i]");
    //Cancel the query with the queryId
    given()
        .pathParam("queryID", queryId)
        .filter(adminSessionFilter)
        .when()
        .get("/profiles/cancel/{queryID}")
        .then()
        .statusCode(200);
    //put 3 seconds sleep time to make sure query is cancelled
    Thread.sleep(3000);
    //Check to make sure that query is cancelled
    given()
        .pathParam("queryID", queryId)
        .filter(adminSessionFilter)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state", equalTo(3));
  }
}
