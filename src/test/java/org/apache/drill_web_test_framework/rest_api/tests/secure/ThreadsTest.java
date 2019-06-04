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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

public class ThreadsTest extends BaseRestTest {
  //Will fail due to DRILL-6690 (non-Admins can access threads page)
  //@Test
  public void nonAdminAccessThreadsPage() {
    given()
        .filter(nonAdminSessionFilter)
        .when()
        .get("/status/threads")
        .then()
        .statusCode(500)
        .body(containsString("HTTP 403 Forbidden"));
  }

  @Test
  public void adminAccessThreadsPage() {
    given()
        .filter(adminSessionFilter)
        .when()
        .get("/status/threads")
        .then()
        .statusCode(200)
        .body(allOf(containsString("Reference Handler")), containsString("Signal Dispatcher"));
  }
}
