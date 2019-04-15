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
package org.apache.drillui.test.framework.steps.webui;

import org.apache.drillui.test.framework.initial.WebBrowser;
import org.apache.drillui.test.framework.pages.BasePage;
import org.apache.drillui.test.framework.pages.ChooseAuthMethodPage;
import org.apache.drillui.test.framework.pages.NavigationPage;

public final class AuthSteps {

  private AuthSteps() {
  }

  public static NavigationPage login(String login, String password) {
    BasePage.getPage(NavigationPage.class)
        .navigateLogin();
    return performPlainAunth(login, password);
  }

  public static NavigationPage loginFromCustomUrl(String url, String login, String password) {
    WebBrowser.openURL(url);
    return performPlainAunth(login, password);
  }

  private static NavigationPage performPlainAunth(String login, String password) {
    return BasePage.getPage(ChooseAuthMethodPage.class)
        .openLoginPage()
        .setUserName(login)
        .setUserPassword(password)
        .submit();
  }

  public static NavigationPage logOut() {
    return BasePage.getPage(NavigationPage.class)
        .navigateLogout();
  }
  public static String getLogoutText() {
    return BasePage.getPage(NavigationPage.class)
        .getLogoutText();
  }
}
