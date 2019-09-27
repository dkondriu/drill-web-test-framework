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
import org.apache.drill.exec.proto.UserBitShared.QueryResult.QueryState;
import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.cancelTheQuery;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.getStringFromResource;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.isQueryInState;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.isQueryRunning;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.waitForCondition;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.testng.Assert.assertTrue;

public class ProfilesTest extends BaseRestTest {
  private static String bigQuery = null;

  @BeforeClass
  public static void getLargeQuery() {
    bigQuery = getStringFromResource("queries/big_query.sql").replaceAll("\n", " ");
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
    assertTrue(waitForCondition(condition -> isQueryRunning("query", bigQuery, adminSessionFilter)));
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
  public void nonAdminCancelOwnQuery() {
    RestBaseSteps.runQueryInBackground(bigQuery, nonAdminSessionFilter);
    assertTrue(waitForCondition(condition -> isQueryRunning("query", bigQuery, nonAdminSessionFilter)));
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
    cancelTheQuery(queryId, nonAdminSessionFilter);
    waitForCondition(condition -> isQueryInState(queryId, QueryState.CANCELED, nonAdminSessionFilter));
    //Check to make sure that query is cancelled
    given()
        .pathParam("queryID", queryId)
        .filter(nonAdminSessionFilter)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state", equalTo(QueryState.CANCELED.getNumber()));
  }

  @Test
  public void adminCancelSecondNonAdminQuery() throws InterruptedException {
    RestBaseSteps.runQueryInBackground(bigQuery, secondNonAdminSessionFilter);
    assertTrue(waitForCondition(condition -> isQueryRunning("query", bigQuery, secondNonAdminSessionFilter)));
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
    cancelTheQuery(queryId, adminSessionFilter);
    waitForCondition(condition -> isQueryInState(queryId, QueryState.CANCELED, adminSessionFilter));
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
