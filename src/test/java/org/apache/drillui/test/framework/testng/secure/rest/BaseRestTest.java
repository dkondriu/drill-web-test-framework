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

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.filter.session.SessionFilter;
import org.apache.drillui.test.framework.initial.PropertiesConst;
import org.apache.drillui.test.framework.initial.TestProperties;
import org.testng.annotations.BeforeSuite;

public class BaseRestTest {
  public static String drillUserName = "mapr1";
  public static String drillUserPassword = "mapr";
  public static String drillSecondUserName = "mapr2";
  public static String drillSecondUserPassword = "mapr";
  public static String drillAdminUserName = "mapr";
  public static String drillAdminUserPassword = "mapr";
  public static SessionFilter adminSessionFilter = new SessionFilter();
  public static SessionFilter nonAdminSessionFilter = new SessionFilter();
  public static SessionFilter secondNonAdminSessionFilter = new SessionFilter();

  @BeforeSuite
  public void initUsers() {
    RestAssured.port = PropertiesConst.DRILL_PORT;
    RestAssured.baseURI = PropertiesConst.DRILL_HOST;

    RestAssured.given()
        .auth()
        .form(drillAdminUserName, drillAdminUserPassword, new FormAuthConfig(
            "/j_security_check",
            "j_username",
            "j_password")
        )
        .filter(adminSessionFilter)
        .when()
        .get("/login");

    RestAssured.given()
        .auth()
        .form(drillUserName, drillUserPassword, new FormAuthConfig(
            "/j_security_check",
            "j_username",
            "j_password"))
        .filter(nonAdminSessionFilter)
        .when()
        .get("/login");

    RestAssured.given()
        .auth()
        .form(drillSecondUserName, drillSecondUserPassword, new FormAuthConfig(
            "/j_security_check",
            "j_username",
            "j_password"))
        .filter(secondNonAdminSessionFilter)
        .when()
        .get("/login");
  }
}
