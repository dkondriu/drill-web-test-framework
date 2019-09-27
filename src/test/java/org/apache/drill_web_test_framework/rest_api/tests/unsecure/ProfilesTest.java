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
import org.apache.drill.exec.proto.UserBitShared.QueryResult.QueryState;
import org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.cancelTheQuery;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.getStringFromResource;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.isQueryInState;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.isQueryRunning;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.waitForCondition;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ProfilesTest extends BaseRestTest {
  private static String bigQuery = null;

  @BeforeClass
  public static void getLargeQuery() {
    bigQuery = getStringFromResource("queries/big_query.sql").replaceAll("\n", " ");
  }

  @Test
  public void checkRunnningQueryProfile() {
    RestBaseSteps.runQueryInBackground(bigQuery, sessionFilter);
    assertTrue(waitForCondition(condition -> isQueryRunning("query", bigQuery, sessionFilter)));
    Response response = get("/profiles.json").then().extract().response();
    //Extract the entries for runningQueries from the response
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    String queryId = "";
    for (int i = 0; i < jsonResponse.size(); i++) {
      if (bigQuery.startsWith(response.jsonPath().param("i", i).getString("runningQueries.query[i]"))) {
        queryId = response.jsonPath().param("i", i).getString("runningQueries.queryId[i]");
        break;
      }
    }
    assertFalse(queryId.isEmpty());
    given()
        .filter(sessionFilter)
        .pathParam("queryID", queryId)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("query", containsString(bigQuery.trim()))
        .body(containsString("majorFragmentId"))
        .body(containsString("minorFragmentId"))
        .body(containsString("minorFragmentProfile"))
        .body(containsString("operatorProfile"));
  }

  @Test
  public void cancelRunningQuery() {
    RestBaseSteps.runQueryInBackground(bigQuery, sessionFilter);
    assertTrue(waitForCondition(condition -> isQueryRunning("query", bigQuery, sessionFilter)));
    Response response = get("/profiles.json").then().extract().response();
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
    cancelTheQuery(queryId, sessionFilter);
    waitForCondition(condition -> isQueryInState(queryId, QueryState.CANCELED, sessionFilter));
    given()
        .filter(sessionFilter)
        .pathParam("queryID", queryId)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state", equalTo(QueryState.CANCELED.getNumber()));
  }
}
