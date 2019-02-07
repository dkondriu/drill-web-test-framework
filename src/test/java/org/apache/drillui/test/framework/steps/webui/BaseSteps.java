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

import org.apache.drillui.test.framework.initial.TestProperties;
import org.apache.drillui.test.framework.initial.WebBrowser;
import org.apache.drillui.test.framework.pages.BasePage;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class BaseSteps {

  private BaseSteps() {
  }

  public static void setImplicitWait(int seconds) {
    WebBrowser.setImplicitWait(seconds);
  }

  public static void resetImplicitWait() {
    WebBrowser.resetImplicitWait();
  }

  public static void tearDown() {
    BasePage.clearPages();
    WebBrowser.closeBrowser();
  }

  public static void openUrl(String url) {
    WebBrowser.openURL(url);
  }

  public static void waitForURL(String url) {
    new WebDriverWait(WebBrowser.getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"))
        .until(driver -> WebBrowser.getURL().equals(url)
    );
  }

  public static String getURL() {
    return WebBrowser.getURL();
  }
}
