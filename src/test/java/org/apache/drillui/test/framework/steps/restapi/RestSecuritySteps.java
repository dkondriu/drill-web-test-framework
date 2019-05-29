/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.drillui.test.framework.steps.restapi;

import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.filter.session.SessionFilter;
import org.apache.drillui.test.framework.initial.PropertiesConst;

public final class RestSecuritySteps {

  private RestSecuritySteps() {
  }

  public static SessionFilter getDefaultSession() {
    return PropertiesConst.SECURE_DRILL ?
        login(PropertiesConst.ADMIN_1_NAME, PropertiesConst.ADMIN_1_PASSWORD) :
        new SessionFilter();
  }

  public static SessionFilter login(String userName, String userPassword) {
    SessionFilter session = new SessionFilter();
    RestAssured.given()
        .auth()
        .form(
            userName,
            userPassword,
            new FormAuthConfig(
                "/j_security_check",
                "j_username",
                "j_password")
        )
        .filter(session)
        .when()
        .get("/login");
    return session;
  }
}
