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

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ProfilesTest extends BaseRestTest {
  // Where to get test data for the query???
  //@Test
  public void checkRunnningQueryProfile() throws InterruptedException {
    String sqlQuery = "select count (distinct ss_customer_sk),count(distinct ss_item_sk),count(distinct ss_store_sk),max(ss_ticket_number),sum(ss_item_sk),max(ss_net_profit) from `dfs.tpcds_sf100_parquet`.store_sales";
    String queryText = "{\"queryType\":\"SQL\", \"query\": \"" + sqlQuery + "\"}";
    given()
        .body(queryText)
        .with()
        .contentType("application/json")
        .when()
        .post("/query.json")
        .then()
        .statusCode(200);
    //put 3 seconds sleep time to make sure the query is in a running state
    Thread.sleep(3000);
    Response response = get("/profiles.json").then().extract().response();
    //Extract the entries for runningQueries from the response
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    int i;
    for(i = 0; i <= jsonResponse.size(); i++) {
      String query = sqlQuery;
      if (query.equalsIgnoreCase(response.jsonPath().param("i", i).getString("runningQueries.query[i]"))) {
        break;
      }
      break;
    }
    String queryId = response.jsonPath().param("i",i).getString("runningQueries.queryId[i]");
    given()
        .pathParam("queryID", queryId)
        .when()
        .get("/profiles/{queryID}.json")
        .then().statusCode(200)
        .body("state", equalTo(1))
        .body("query", containsString("select count (distinct ss_customer_sk),count(distinct ss_item_sk),count(distinct ss_store_sk),max(ss_ticket_number),sum(ss_item_sk),max(ss_net_profit)"))
        .body(containsString("majorFragmentId"))
        .body(containsString("minorFragmentId"))
        .body(containsString("minorFragmentProfile"))
        .body(containsString("operatorProfile"));
  }
  // Where to get test data for the query???
  //@Test
  public void cancelRunningQuery() throws InterruptedException {
    String sqlQuery = "select max(ss_sold_date_sk),min(ss_sold_time_sk),count(distinct ss_store_sk) from `dfs.tpcds_sf100_parquet`.store_sales";
    String queryText = "{\"queryType\":\"SQL\", \"query\": \"" + sqlQuery + "\"}";
    given()
        .body(queryText)
        .with()
        .contentType("application/json")
        .when()
        .post("/query.json")
        .then()
        .statusCode(200);
    //put 2 seconds sleep time here to make sure query is in running state
    Thread.sleep(2000);
    Response response = get("/profiles.json").then().extract().response();
    //Extract the entries for runningQueries from the response
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    int i;
    for(i = 0; i <= jsonResponse.size(); i++) {
      String query = sqlQuery;
      if (query.equalsIgnoreCase(response.jsonPath().param("i", i).getString("runningQueries.query[i]"))) {
        break;
      }
      break;
    }
    String queryId = response.jsonPath().param("i",i).getString("runningQueries.queryId[i]");
    //Cancel the query with the queryId
    given()
        .pathParam("queryID", queryId)
        .when()
        .get("/profiles/cancel/{queryID}")
        .then()
        .statusCode(200);
    //put 10 seconds sleep time here for query to completely cancelled
    Thread.sleep(10000);
    given()
        .pathParam("queryID", queryId)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state",equalTo(3));
  }
}
