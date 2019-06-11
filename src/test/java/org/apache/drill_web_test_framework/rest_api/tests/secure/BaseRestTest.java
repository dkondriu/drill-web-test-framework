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

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.filter.session.SessionFilter;
import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.testng.annotations.BeforeSuite;

public class BaseRestTest {
  public static SessionFilter adminSessionFilter = new SessionFilter();
  public static SessionFilter nonAdminSessionFilter = new SessionFilter();
  public static SessionFilter secondNonAdminSessionFilter = new SessionFilter();

  @BeforeSuite
  public void initUsers() {
    RestAssured.port = PropertiesConst.DRILL_PORT;
    RestAssured.baseURI = PropertiesConst.DRILL_HOST;

    RestAssured.given()
        .auth()
        .form(PropertiesConst.ADMIN_1_NAME, PropertiesConst.ADMIN_1_PASSWORD, new FormAuthConfig(
            "/j_security_check",
            "j_username",
            "j_password")
        )
        .filter(adminSessionFilter)
        .when()
        .get("/login");

    RestAssured.given()
        .auth()
        .form(PropertiesConst.USER_1_NAME, PropertiesConst.USER_1_PASSWORD, new FormAuthConfig(
            "/j_security_check",
            "j_username",
            "j_password"))
        .filter(nonAdminSessionFilter)
        .when()
        .get("/login");

    RestAssured.given()
        .auth()
        .form(PropertiesConst.USER_2_NAME, PropertiesConst.USER_2_PASSWORD, new FormAuthConfig(
            "/j_security_check",
            "j_username",
            "j_password"))
        .filter(secondNonAdminSessionFilter)
        .when()
        .get("/login");
  }
}
