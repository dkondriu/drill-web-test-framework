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
package org.apache.drillui.test.framework.pages;

import org.apache.drillui.test.framework.initial.TestProperties;
import org.apache.drillui.test.framework.initial.WebBrowser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NavigationPage extends BasePage {
  @FindBy(partialLinkText = "Apache Drill")
  private WebElement home;

  @FindBy(partialLinkText = "Query")
  private WebElement query;

  @FindBy(partialLinkText = "Profiles")
  private WebElement profiles;

  @FindBy(partialLinkText = "Storage")
  private WebElement storage;

  @FindBy(partialLinkText = "Metrics")
  private WebElement metrics;

  @FindBy(partialLinkText = "Threads")
  private WebElement threads;

  @FindBy(partialLinkText = "Logs")
  private WebElement logs;

  @FindBy(partialLinkText = "Options")
  private WebElement options;

  @FindBy(partialLinkText = "Documentation")
  private WebElement documentation;

  @FindBy(partialLinkText = "Log In")
  private WebElement login;

  @FindBy(partialLinkText = "Log Out (")
  private WebElement logout;

  public void navigateHome() {
    home.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
  }

  public QueryPage navigateQuery() {
    query.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
    return getPage(QueryPage.class);
  }

  public void navigateProfiles() {
    profiles.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
  }

  public void navigateStorage() {
    storage.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
  }

  public void navigateMetrics() {
    metrics.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
  }

  public void navigateThreads() {
    threads.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
  }

  public void navigateLogs() {
    logs.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
  }

  public void navigateOptions() {
    options.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
  }

  public void navigateDocumentation() {
    documentation.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
  }

  public ChooseAuthMethodPage navigateLogin() {
    login.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
    return getPage(ChooseAuthMethodPage.class);
  }

  public NavigationPage navigateLogout() {
    logout.click();
    WebBrowser.waitSeconds(TestProperties.defaultTimeout);
    return getPage(NavigationPage.class);
  }

  public String getLoginText() {
    return login.getText();
  }

  public String getLogoutText() {
    return logout.getText();
  }

}
