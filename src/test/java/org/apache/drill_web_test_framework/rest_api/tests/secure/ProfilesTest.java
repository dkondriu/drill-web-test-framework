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
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;

public class ProfilesTest extends BaseRestTest {
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
        .body("finishedQueries.user", everyItem(equalTo(drillUserName)));
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
        .body("finishedQueries.user", hasItems(drillUserName, drillSecondUserName, drillAdminUserName));
  }
  // Where to get test data for the query???
  //@Test
  public void nonAdminCancelSecondNonAdminQuery() throws InterruptedException {
    String query = "SELECT COUNT(DISTINCT c_customer_sk) as c_customer_sk FROM `dfs.tpcds_sf100_parquet`.customer WHERE c_customer_sk IN (SELECT SS_CUSTOMER_SK FROM `dfs.tpcds_sf100_parquet`.store_sales)";
    String queryText = "{\"queryType\":\"SQL\", \"query\": \"" + query + "\"}";
    //Run the query
    given()
        .filter(secondNonAdminSessionFilter)
        .body(queryText)
        .with()
        .contentType("application/json")
        .post("/query.json")
        .then()
        .statusCode(200);
    //put 3 seconds sleep time here to make sure query is in running state
    Thread.sleep(3000);
    //Get the response
    Response response = given().filter(adminSessionFilter).get("/profiles.json").then().extract().response();
    //Extract the entries for runningQueries from the response
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    int i;
    for(i = 0; i <= jsonResponse.size(); i++) {
      if (query.equalsIgnoreCase(response.jsonPath().param("i", i).getString("runningQueries.query[i]"))) {
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
        .statusCode(200)
        .body(containsString("Failure attempting to cancel query" + " " + queryId));
    //put 5 seconds sleep time
    Thread.sleep(5000);
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
  // Where to get test data for the query???
  //@Test
  public void nonAdminCancelOwnQuery() throws InterruptedException {
    String query = "select t.ss_customer_sk,t.ss_store_sk,t.ss_sold_date_sk,cast (sum(t.ss_sales_price)/sum(t.ss_quantity) as decimal (25,20)) as avg_price from `dfs.tpcds_sf100_parquet`.store_sales t where (t.ss_item_sk in (select i_item_sk from `dfs.tpcds_sf100_parquet`.item where i_manufact_id = 10 or i_category_id = 5) or t.ss_item_sk in (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25)) and t.ss_store_sk in (1, 2, 3) group by t.ss_customer_sk,t.ss_store_sk,t.ss_sold_date_sk having sum(t.ss_sales_price)/sum(t.ss_quantity) >= 50.0 order by t.ss_customer_sk,t.ss_store_sk,t.ss_sold_date_sk";
    String queryText = "{\"queryType\":\"SQL\", \"query\": \"" + query + "\"}";
    //Run the shell script
    given()
        .filter(nonAdminSessionFilter)
        .body(queryText)
        .with()
        .contentType("application/json")
        .post("/query.json")
        .then()
        .statusCode(200);
    //put 5 seconds sleep time here to make sure query is in running state
    Thread.sleep(5000);
    //Get the response
    Response response = given().filter(nonAdminSessionFilter).get("/profiles.json").then().extract().response();
    //Put the running queries into a list
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    int i;
    for(i = 0; i <= jsonResponse.size(); i++) {
      if (query.equalsIgnoreCase(response.jsonPath().param("i", i).getString("runningQueries.query[i]"))) {
        break;
      }
      break;
    }
    String queryId = response.jsonPath().param("i",i).getString("runningQueries.queryId[i]");
    //Cancel the query with the queryId
    given()
        .pathParam("queryID", queryId)
        .filter(nonAdminSessionFilter)
        .when()
        .get("/profiles/cancel/{queryID}")
        .then()
        .statusCode(200);
    //put 10 seconds sleep time to make sure query is cancelled
    Thread.sleep(10000);
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
  // Where to get test data for the query???
  //@Test
  public void adminCancelSecondNonAdminQuery() throws InterruptedException {
    String query = "select t.wr_returning_customer_sk,t.wr_returned_date_sk,cast(sum(t.wr_return_amt)/sum(t.wr_return_quantity) as bigint) as avg_return_amt from `dfs.tpcds_sf100_parquet`.web_returns t where (t.wr_item_sk in (select ws_item_sk from `dfs.tpcds_sf100_parquet`.web_sales where ws_sales_price < 50 or ws_ext_sales_price < 1000) or t.wr_item_sk in (1, 2, 3, 4, 5, 6, 7, 8, 9, 10)) and t.wr_reason_sk in (10) group by t.wr_returning_customer_sk,t.wr_returned_date_sk having sum(t.wr_return_amt)/sum(t.wr_return_quantity) >= 50.0 order by t.wr_returning_customer_sk,t.wr_returned_date_sk";
    String queryText = "{\"queryType\":\"SQL\", \"query\": \"" + query + "\"}";
    //Run the shell script
    given()
        .filter(adminSessionFilter)
        .body(queryText)
        .with()
        .contentType("application/json")
        .post("/query.json")
        .then()
        .statusCode(200);
    //put 5 seconds sleep time here to make sure query is in running state
    Thread.sleep(5000);
    //Get the response
    Response response = given().filter(secondNonAdminSessionFilter).get("/profiles.json").then().extract().response();
    //Put the running queries into a list
    List<String> jsonResponse = response.jsonPath().getList("runningQueries");
    //Iterate through the repsonse to find the matching running query
    int i;
    for(i = 0; i <= jsonResponse.size(); i++) {
      if (query.equalsIgnoreCase(response.jsonPath().param("i",i).getString("runningQueries.query[i]"))) {
        break;
      }
      break;
    }
    String queryId = response.jsonPath().param("i",i).getString("runningQueries.queryId[i]");
    //Cancel the query with the queryId
    given()
        .pathParam("queryID", queryId)
        .filter(adminSessionFilter)
        .when()
        .get("/profiles/cancel/{queryID}")
        .then()
        .statusCode(200);
    //put 10 seconds sleep time to make sure query is cancelled
    Thread.sleep(10000);
    //Check to make sure that query is cancelled
    given()
        .pathParam("queryID", queryId)
        .filter(adminSessionFilter)
        .when()
        .get("/profiles/{queryID}.json")
        .then()
        .statusCode(200)
        .body("state",equalTo(3));
  }
}
