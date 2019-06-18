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
package org.apache.drill_web_test_framework.web_ui.steps;

import org.apache.drill_web_test_framework.web_ui.pages.BasePage;
import org.apache.drill_web_test_framework.web_ui.pages.OptionsPage;

import java.util.List;

public final class OptionsSteps extends BaseSteps {

  private static OptionsPage getOptionsPage() {
    return BasePage.getPage(OptionsPage.class);
  }

  public OptionsSteps searchFieldSendKeys(String string) {
    getOptionsPage().searchFieldSendKeys(string);
    return getSteps(OptionsSteps.class);
  }

  public OptionsSteps clearSearchField() {
    getOptionsPage().clearSearchField();
    return getSteps(OptionsSteps.class);
  }

  public OptionsSteps sortByOption() {
    getOptionsPage().sortByOption();
    return getSteps(OptionsSteps.class);
  }

  public OptionsSteps selectQuickFilter(String filter) {
    getOptionsPage().selectQuickFilter(filter);
    return getSteps(OptionsSteps.class);
  }

  public List<List<String>> getOptionsTable() {
    return getOptionsPage().getOptionsTable();
  }

  public boolean isSearchFieldFilled(String string) {
    return getOptionsPage().isSearchFieldFilled(string);
  }

  public OptionsSteps setOptionToDefault(String optionName) {
    getOptionsPage().setOptionToDefault(optionName);
    return getSteps(OptionsSteps.class);
  }

  public OptionsSteps updateBooleanTrueOrFalse(String optionName, String trueOrFalse) {
    getOptionsPage().updateBooleanTrueOrFalse(optionName, trueOrFalse);
    return getSteps(OptionsSteps.class);
  }

  public OptionsSteps updateFieldOption(String optionName, String value) {
    getOptionsPage().updateFieldOption(optionName, value);
    return getSteps(OptionsSteps.class);
  }

  public boolean isValueAppropriate(String optionName, String value) {
    return getOptionsPage().isValueAppropriate(optionName, value);
  }

  public boolean isBooleanValueAppropriate(String optionName, String trueOrFalse) {
    return getOptionsPage().isBooleanValueAppropriate(optionName, trueOrFalse);
  }

}
