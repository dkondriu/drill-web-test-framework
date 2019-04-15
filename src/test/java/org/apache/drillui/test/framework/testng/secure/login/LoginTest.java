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
package org.apache.drillui.test.framework.testng.secure.login;

import org.apache.drillui.test.framework.initial.TestProperties;
import org.apache.drillui.test.framework.steps.webui.BaseSteps;
import org.apache.drillui.test.framework.testng.secure.BaseSecureTest;
import org.testng.annotations.Test;
import org.apache.drillui.test.framework.steps.webui.AuthSteps;

import static org.testng.Assert.*;

public class LoginTest extends BaseSecureTest {
  @Test(groups = {"functional"})
  public void testLogin() {
    assertEquals(
        AuthSteps.login(TestProperties.get("ADMIN_USER1_NAME"), TestProperties.get("ADMIN_USER1_PASSWORD")).getLogoutText(),
        "Log Out (" + TestProperties.get("ADMIN_USER1_NAME") + ")", "Login failed");
  }

  @Test(groups = {"functional"}, dependsOnMethods = {"testLogin"})
  public void testLogout() {
    assertEquals(AuthSteps.logOut().getLoginText(), "Log In", "Logout failed");
  }

  @Test(groups = {"functional"}, dependsOnMethods = {"testLogout"})
  public void testRedirect() {
    //
    AuthSteps.loginFromCustomUrl("/mainLogin?redirect=https%3A%2F%2Fyoutube.com", TestProperties.get("DRILL_USER_NAME"), TestProperties.get("DRILL_USER_PASSWORD"));
    assertEquals(BaseSteps.getURL(), "/", "Redirection outside the Drill after login!");
    assertEquals(AuthSteps.getLogoutText(), "Log Out (" + TestProperties.get("DRILL_USER_NAME") + ")", "Login from custom URL failed");
  }

  @Test(groups = {"functional"}, dependsOnMethods = {"testRedirect"})
  public void testLogoutAfterRedirectLogin() {
    assertEquals(AuthSteps.logOut().getLoginText(), "Log In", "Logout failed");
  }
}
