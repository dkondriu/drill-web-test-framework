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

import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.runQuery;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.testng.Assert.assertTrue;

public class MetricsTest extends BaseRestTest {
  @BeforeClass
  public void runSimpleQuery() {
    runQuery("SELECT * FROM cp.`employee.json` LIMIT 2", sessionFilter);
  }

  @Test
  public void checkMetricsPage() {
    ValidatableResponse response = given()
        .filter(sessionFilter)
        .when()
        .get("/status/metrics")
        .then()
        .statusCode(200)
        .body("gauges.count.value", greaterThan(0))
        .body("gauges.'daemon.count'.value", greaterThan(0))
        .body("counters.'drill.connections.rpc.control.unencrypted'.count", greaterThanOrEqualTo(0))
        .body("counters.'drill.connections.rpc.user.unencrypted'.count", greaterThanOrEqualTo(0))
        .body("counters.'drill.queries.completed'.count", greaterThan(0))
        .body("histograms.'drill.allocator.huge.hist'.count", greaterThanOrEqualTo(0))
        .body("histograms.'drill.allocator.normal.hist'.max", greaterThan(0))
        .body("histograms.'drill.allocator.normal.hist'.stddev", greaterThan(0.0f));
    assertTrue(Long.parseLong(response.extract().jsonPath().getString("gauges.'heap.used'.value")) > 0L);
  }
}
