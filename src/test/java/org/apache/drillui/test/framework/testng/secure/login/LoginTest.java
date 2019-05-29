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

import org.apache.drillui.test.framework.initial.PropertiesConst;
import org.apache.drillui.test.framework.steps.webui.AuthSteps;
import org.apache.drillui.test.framework.steps.webui.BaseSteps;
import org.apache.drillui.test.framework.steps.webui.ProfilesSteps;
import org.apache.drillui.test.framework.steps.webui.QueryResultsSteps;
import org.apache.drillui.test.framework.testng.secure.BaseSecureTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

public class LoginTest extends BaseSecureTest {
  private AuthSteps authSteps = BaseSteps.getSteps(AuthSteps.class);
  private final QueryResultsSteps queryResultsSteps = BaseSteps.getSteps(QueryResultsSteps.class);
  private ProfilesSteps profilesSteps = BaseSteps.getSteps(ProfilesSteps.class);
  private String defaultQueryString = "SELECT * FROM cp.`employee.json` LIMIT 20";
  private String completedStatus = "COMPLETED";

  @BeforeClass(groups = {"functional"})
  public void loginAndLogout() {
    assertEquals(authSteps.login(PropertiesConst.ADMIN_1_NAME, PropertiesConst.ADMIN_1_PASSWORD)
        .getLogoutText(), "Log Out (" + PropertiesConst.ADMIN_1_NAME + ")", "Login failed");
    assertEquals(authSteps.logOut().getLoginText(), "Log In", "Logout failed");
  }

  @Test(groups = {"functional"})
  public void adminsViewVsUsersViewInSecurityMode() {
    //Step 1. Run query for Admin1
    assertEquals(authSteps.login(PropertiesConst.ADMIN_1_NAME, PropertiesConst.ADMIN_1_PASSWORD).navigateQuery()
        .submitQuery(defaultQueryString)
        .getQueryStatus(), completedStatus);
    String admin_1_QueryProfile = queryResultsSteps.getQueryProfile();
    authSteps.logOut();

    //Step 2. Run query for Admin2
    assertEquals(authSteps.login(PropertiesConst.ADMIN_2_NAME, PropertiesConst.ADMIN_2_PASSWORD).navigateQuery()
        .submitQuery(defaultQueryString)
        .getQueryStatus(), completedStatus);
    String admin_2_QueryProfile = queryResultsSteps.getQueryProfile();
    authSteps.logOut();

    //Step 3. Run query for User1
    assertEquals(authSteps.login(PropertiesConst.USER_1_NAME, PropertiesConst.USER_1_PASSWORD).navigateQuery()
        .submitQuery(defaultQueryString)
        .getQueryStatus(), completedStatus);
    String user_1_QueryProfile = queryResultsSteps.getQueryProfile();
    authSteps.logOut();

    //Step 4. Run query for User2
    assertEquals(authSteps.login(PropertiesConst.USER_2_NAME, PropertiesConst.USER_2_PASSWORD).navigateQuery()
        .submitQuery(defaultQueryString)
        .getQueryStatus(), completedStatus);
    String user_2_QueryProfile = queryResultsSteps.getQueryProfile();
    authSteps.logOut();

    //Step 5. Checks that Admin1 can see query of all Admins/Users
    assertTrue(authSteps.login(PropertiesConst.ADMIN_1_NAME, PropertiesConst.ADMIN_1_PASSWORD).navigateProfiles().isQueryProfileINList(admin_1_QueryProfile));
    assertTrue(profilesSteps.isQueryProfileINList(admin_2_QueryProfile));
    assertTrue(profilesSteps.isQueryProfileINList(user_1_QueryProfile));
    assertTrue(profilesSteps.isQueryProfileINList(user_2_QueryProfile));
    authSteps.logOut();

    //Step 6. Checks that User can see only his queries via assertTrue
    assertTrue(authSteps.login(PropertiesConst.USER_1_NAME, PropertiesConst.USER_1_PASSWORD).navigateProfiles().isQueryProfileINList(user_1_QueryProfile));

    // and DO NOT SEE other queries via assertFalse
    profilesSteps.selectAllInDropDownList();
    assertFalse(profilesSteps.isQueryProfileINList(user_2_QueryProfile));
    assertFalse(profilesSteps.isQueryProfileINList(admin_1_QueryProfile));
    assertFalse(profilesSteps.isQueryProfileINList(admin_2_QueryProfile));
    assertTrue(profilesSteps.isUsersNameInProfileList(PropertiesConst.USER_1_NAME));
    authSteps.logOut();
  }

  @Test(groups = {"functional"})
  public void redirect() {
    authSteps.loginFromCustomUrl("/mainLogin?redirect=https%3A%2F%2Fyoutube.com", PropertiesConst.USER_1_NAME, PropertiesConst.USER_1_PASSWORD);
    assertEquals(BaseSteps.getURL(), "/", "Redirection outside the Drill after login!");
    assertEquals(authSteps.getLogoutText(), "Log Out (" + PropertiesConst.USER_1_NAME + ")", "Login from custom URL failed");
    assertEquals(authSteps.logOut().getLoginText(), "Log In", "Logout failed");
  }
}
