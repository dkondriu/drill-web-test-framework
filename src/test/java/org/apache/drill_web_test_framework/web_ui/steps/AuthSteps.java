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
package org.apache.drill_web_test_framework.web_ui.steps;

import org.apache.drill_web_test_framework.web_ui.WebBrowser;
import org.apache.drill_web_test_framework.web_ui.pages.ChooseAuthMethodPage;
import org.apache.drill_web_test_framework.web_ui.pages.NavigationPage;

import static org.apache.drill_web_test_framework.web_ui.pages.BasePage.getPage;

public final class AuthSteps extends BaseSteps {

  public NavigationSteps login(String login, String password) {
    getNavigationPage()
        .navigateLogin();
    return performPlainAuth(login, password);
  }

  public NavigationSteps loginFromCustomUrl(String url, String login, String password) {
    WebBrowser.openURL(url);
    return performPlainAuth(login, password);
  }

  private NavigationSteps performPlainAuth(String login, String password) {
    getPage(ChooseAuthMethodPage.class)
        .openLoginPage()
        .setUserName(login)
        .setUserPassword(password)
        .submit();
    return getNavigationSteps();
  }

  public NavigationSteps logOut() {
    return getNavigationSteps()
        .navigateLogout();
  }

  public String getLogoutText() {
    return getNavigationSteps()
        .getLogoutText();
  }

  private NavigationPage getNavigationPage() {
    return getPage(NavigationPage.class);
  }

  private NavigationSteps getNavigationSteps() {
    return getSteps(NavigationSteps.class);
  }
}
