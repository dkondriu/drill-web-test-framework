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

package org.apache.drillui.test.framework.pages;

import org.apache.drillui.test.framework.initial.TestProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QueryProfileDetailsPage extends BasePage{

  @FindBy(id = "query-tabs")
  private WebElement tabs;

  @FindBy(id = "query-text")
  private WebElement queryText;

  public QueryProfileDetailsPage navigateTab(String tabText) {
    tabs.findElement(By.linkText(tabText))
        .click();
    return this;
  }

  public String activeQuery() {
    return tabs.findElement(By.className("active"))
        .getText();
  }

  public String getQueryText() {
    waitForEditorText(queryText);
    return getEditorText(queryText);
  }

  public void setQueryText(String text) {
    waitForEditorText(queryText);
  }

  private void waitForEditorText(WebElement editor) {
    if (getEditorText(editor).equals("")) {
      new WebDriverWait(getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"))
          .until(driver -> !getEditorText(editor).equals(""));
    }
  }

  private String getEditorText(WebElement editor) {
    return editor.findElement(By.className("ace_content"))
        .getText();
  }
}
