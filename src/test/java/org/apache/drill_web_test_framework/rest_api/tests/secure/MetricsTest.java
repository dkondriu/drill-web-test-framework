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

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class MetricsTest extends BaseRestTest {
  @Test
  public void nonAdminMetricsPage() {
    given()
        .filter(nonAdminSessionFilter)
        .when()
        .get("/status/metrics")
        .then()
        .statusCode(200)
        .body("gauges.count.value", greaterThan(0))
        .body("gauges.'daemon.count'.value", greaterThan(0))
        .body("gauges.'heap.used'.value", greaterThan(0))
        .body("counters.'drill.connections.rpc.control.unencrypted'.count", greaterThanOrEqualTo(0))
        .body("counters.'drill.connections.rpc.user.unencrypted'.count", greaterThanOrEqualTo(0))
        .body("counters.'drill.queries.completed'.count", greaterThan(0))
        .body("histograms.'drill.allocator.huge.hist'.count", greaterThanOrEqualTo(0));
  }

  @Test
  public void adminMetricsPage() {
    given()
        .filter(adminSessionFilter)
        .when()
        .get("/status/metrics")
        .then()
        .statusCode(200)
        .body("gauges.count.value", greaterThan(0))
        .body("gauges.'daemon.count'.value", greaterThan(0))
        .body("gauges.'heap.used'.value", greaterThan(0))
        .body("counters.'drill.connections.rpc.control.unencrypted'.count", greaterThanOrEqualTo(0))
        .body("counters.'drill.connections.rpc.user.unencrypted'.count", greaterThanOrEqualTo(0))
        .body("counters.'drill.queries.completed'.count", greaterThan(0))
        .body("histograms.'drill.allocator.huge.hist'.count", greaterThanOrEqualTo(0))
        .body("histograms.'drill.allocator.normal.hist'.max", greaterThan(0));
  }
}
