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
package org.apache.drillui.test.framework.steps.webui;

import org.apache.drillui.test.framework.pages.BasePage;
import org.apache.drillui.test.framework.pages.EditStoragePluginPage;

public class EditStoragePluginSteps {

  private EditStoragePluginSteps() {
  }

  private static EditStoragePluginPage getPage() {
    return BasePage.getPage(EditStoragePluginPage.class);
  }

  public static void setPluginConfig(String pluginConfig) {
    getPage()
        .setPluginConfig(pluginConfig);
  }

  public static void back() {
    getPage()
        .back();
  }

  public static boolean addPluginMode() {
    BaseSteps.setImplicitWait(0);
    boolean result = getPage().backButtonPresented() &&
        getPage().createButtonPresented() &&
        !getPage().updateButtonPresented() &&
        !getPage().disableButtonPresented() &&
        !getPage().exportButtonPresented() &&
        !getPage().deleteButtonPresented();
    BaseSteps.resetImplicitWait();
    return result;
  }

  public static boolean editPluginMode() {
    BaseSteps.setImplicitWait(0);
    boolean result = getPage().backButtonPresented() &&
        !getPage().createButtonPresented() &&
        getPage().updateButtonPresented() &&
        (getPage().disableButtonPresented() ^
            getPage().enableButtonPresented()) &&
        getPage().exportButtonPresented() &&
        getPage().deleteButtonPresented();
    BaseSteps.resetImplicitWait();
    return result;
  }

  public static boolean enabled() {
    BaseSteps.setImplicitWait(0);
    boolean result = getPage().disableButtonPresented() &&
        !getPage().enableButtonPresented();
    BaseSteps.resetImplicitWait();
    return result;
  }

  public static boolean disabled() {
    BaseSteps.setImplicitWait(0);
    boolean result = !getPage().disableButtonPresented() &&
        getPage().enableButtonPresented();
    BaseSteps.resetImplicitWait();
    return result;
  }

  public static boolean waitForEnabled() {
    getPage().waitForEnabled();
    return enabled();
  }

  public static boolean waitForDisabled() {
    getPage().waitForDisabled();
    return disabled();
  }

  public static String getPluginConfig() {
    return getPage().getPluginConfig();
  }

  public static void create() {
    getPage().create();
  }

  public static void update() {
    getPage().update();
  }

  public static void enable() {
    getPage().enable();
  }

  public static void disable() {
    getPage().disable();
  }

  public static void delete() {
    getPage().delete();
  }

  public static String getMessage() {
    return getPage().getMessage();
  }
}
