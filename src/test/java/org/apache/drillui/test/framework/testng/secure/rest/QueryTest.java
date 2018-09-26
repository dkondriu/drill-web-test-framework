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
package org.apache.drillui.test.framework.testng.secure.rest;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class QueryTest extends BaseRestTest {
  @Test
  public void adminRunQuery() {
    given()
        .filter(adminSessionFilter)
        .body("{\"queryType\":\"SQL\",\"query\":\"SELECT * FROM cp.`employee.json` LIMIT 2\"}")
        .with()
        .contentType("application/json")
        .post("/query.json")
        .then()
        .statusCode(200)
        .body("columns[0]", equalTo("employee_id"))
        .body("rows.employee_id[0]",equalTo("1"));
  }

  @Test
  public void nonAdminRunQuery() {
    given()
        .filter(nonAdminSessionFilter)
        .body("{\"queryType\":\"SQL\",\"query\":\"SELECT * FROM cp.`employee.json` LIMIT 2\"}")
        .with()
        .contentType("application/json")
        .post("/query.json")
        .then()
        .statusCode(200)
        .body("columns[0]", equalTo("employee_id"))
        .body("rows.employee_id[0]",equalTo("1"));
  }

  @Test
  public void secondNonAdminRunQuery() {
    given()
        .filter(secondNonAdminSessionFilter)
        .body("{\"queryType\":\"SQL\",\"query\":\"SELECT * FROM cp.`employee.json` LIMIT 2\"}")
        .with()
        .contentType("application/json")
        .post("/query.json")
        .then()
        .statusCode(200)
        .body("columns[0]", equalTo("employee_id"))
        .body("rows.employee_id[0]",equalTo("1"));
  }
}
