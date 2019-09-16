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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

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
  public void checkRunnningQueryProfile() throws InterruptedException {
    RestBaseSteps.runQueryInBackground(bigQuery, sessionFilter);
    //put 10 seconds sleep time to make sure the query is in a running state
    Thread.sleep(10000);
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
    given()
        .filter(sessionFilter)
        .pathParam("queryID", queryId)
        .when()
        .get("/profiles/{queryID}.json")
        .then().statusCode(200)
        .body("state", equalTo(1))
        //.body("query", containsString("select count (distinct ss_customer_sk),count(distinct ss_item_sk),count(distinct ss_store_sk),max(ss_ticket_number),sum(ss_item_sk),max(ss_net_profit)"))
        .body("query", containsString(bigQuery.trim()))
        .body(containsString("majorFragmentId"))
        .body(containsString("minorFragmentId"))
        .body(containsString("minorFragmentProfile"))
        .body(containsString("operatorProfile"));
  }

  @Test
  public void cancelRunningQuery() throws InterruptedException {
    RestBaseSteps.runQueryInBackground(bigQuery, sessionFilter);
    //put 10 seconds sleep time here to make sure query is in running state
    Thread.sleep(10000);
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
    //Cancel the query with the queryId
    given()
        .filter(sessionFilter)
        .pathParam("queryID", queryId)
        .when()
        .get("/profiles/cancel/{queryID}")
        .then()
        .statusCode(200);
    //put 5 seconds sleep time here for query to completely cancelled
    Thread.sleep(5000);
    given()
        .filter(sessionFilter)
        .pathParam("queryID", queryId)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state", equalTo(3));
  }
}
