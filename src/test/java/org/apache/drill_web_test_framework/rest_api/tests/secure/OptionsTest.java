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
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;

public class OptionsTest extends BaseRestTest {
  //Will fail due to DRILL-6701 (non-admins can access options page)
  //@Test
  public void nonAdminAccessOptionsPage() {
    given()
        .filter(nonAdminSessionFilter)
        .when()
        .get("/options.json")
        .then()
        .statusCode(500)
        .body(containsString("HTTP 403 Forbidden"));
  }
  @Test
  public void adminAccessOptionsPage() {
    given()
        .filter(adminSessionFilter)
        .when()
        .get("/options.json")
        .then()
        .statusCode(200)
        .body("name", hasItems("planner.width.max_per_query", "drill.exec.storage.implicit.filename.column.label"))
        .body("value", hasItems(true, false, 64, "filename", "DEFAULT"))
        .body("accessibleScopes", hasItems("SYSTEM", "ALL"))
        .body("kind", hasItems("BOOLEAN", "LONG", "STRING", "DOUBLE"));
  }
}
