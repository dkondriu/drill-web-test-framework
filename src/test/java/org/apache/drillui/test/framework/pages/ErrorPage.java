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

import org.apache.drillui.test.framework.initial.PropertiesConst;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ErrorPage extends BasePage {
  @FindBy(id = "/errorMessage")
  private WebElement errorElementFirefox;

  @FindBy(xpath = "/html/body/pre")
  private WebElement errorElementChrome;

  /**
   * If this method returns not an empty string, WebDriver opens Drill main page by calling WebBrowser.openURL("/");
   * @return JSON text with Drill WebUI error message
   */
  public String getFullStackTrace() {
    WebElement errorHTMLElement = getBrowserErrorElement();
    if (errorHTMLElement == null) {
      return "";
    }
    try {
      return errorHTMLElement.getText();
    } catch (NoSuchElementException e) {
      return "";
    }
  }

  private WebElement getBrowserErrorElement() {
    switch (PropertiesConst.DRIVER_TYPE) {
      case "FIREFOX":
        return errorElementFirefox;
      case "CHROME":
        return errorElementChrome;
      case "EDGE":
        return errorElementChrome;
      default:
        return null;
    }
  }
}
