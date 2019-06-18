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

import org.apache.drill_web_test_framework.web_ui.pages.NavigationPage;

import static org.apache.drill_web_test_framework.web_ui.pages.BasePage.getPage;

public final class NavigationSteps extends BaseSteps {

  public QuerySteps navigateQuery() {
    getNavigationPage().navigateQuery();
    return getSteps(QuerySteps.class);
  }

  public StorageSteps navigateStorage() {
    getNavigationPage().navigateStorage();
    return getSteps(StorageSteps.class);
  }

  public ProfilesSteps navigateProfiles() {
    getNavigationPage().navigateProfiles();
    return getSteps(ProfilesSteps.class);
  }

  public NavigationSteps navigateLogout() {
    getNavigationPage().navigateLogout();
    return getSteps(NavigationSteps.class);
  }

  public OptionsSteps navigateOptions() {
    getNavigationPage().navigateOptions();
    return getSteps(OptionsSteps.class);
  }

  public String getLoginText() {
    return getNavigationPage().getLoginText();
  }

  public String getLogoutText() {
    return getNavigationPage().getLogoutText();
  }

  private NavigationPage getNavigationPage() {
    return getPage(NavigationPage.class);
  }
}
