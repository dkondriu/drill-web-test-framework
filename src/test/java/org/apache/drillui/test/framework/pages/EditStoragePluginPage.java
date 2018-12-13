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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class EditStoragePluginPage extends BasePage {

  @FindBy(className = "ace_text-input")
  private WebElement textArea;

  @FindBy(className = "ace_content")
  private WebElement textAreaContent;

  @FindBy(linkText = "Back")
  private List<WebElement> backButton;

  @FindBy(xpath = "//button[contains(text(), 'Update')]")
  private List<WebElement> updateButton;

  @FindBy(linkText = "Disable")
  private List<WebElement> disableButton;

  @FindBy(linkText = "Export")
  private List<WebElement> exportButton;

  @FindBy(linkText = "Delete")
  private List<WebElement> deleteButton;

  @FindBy(xpath = "//button[contains(text(), 'Create')]")
  private List<WebElement> createButton;

  @FindBy(linkText = "Enable")
  private List<WebElement> enableButton;

  @FindBy(id = "message")
  private WebElement message;

  public EditStoragePluginPage setPluginConfig(String pluginConfig) {
    waitForTextAreaLoaded();
    textArea.clear();
    textArea.sendKeys(pluginConfig);
    return this;
  }

  public String getPluginConfig() {
    waitForTextAreaLoaded();
    return getTextAreaContent();
  }

  public void back() {
    backButton.get(0).click();
  }

  public boolean backButtonPresented() {
    return backButton.size() > 0;
  }

  public boolean createButtonPresented() {
    return createButton.size() > 0;
  }

  public boolean updateButtonPresented() {
    return updateButton.size() > 0;
  }

  public boolean enableButtonPresented() {
    return enableButton.size() > 0;
  }

  public boolean disableButtonPresented() {
    return disableButton.size() > 0;
  }

  public boolean exportButtonPresented() {
    return exportButton.size() > 0;
  }

  public boolean deleteButtonPresented() {
    return deleteButton.size() > 0;
  }

  public void create() {
    createButton.get(0).click();
    new WebDriverWait(getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"))
        .until(ExpectedConditions.stalenessOf(createButton.get(0)));
  }

  public void update() {
    updateButton.get(0).click();
  }

  public void enable() {
    enableButton.get(0).click();
  }

  public void disable() {
    disableButton.get(0).click();
  }

  public void delete() {
    deleteButton.get(0).click();
    new WebDriverWait(getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"))
        .until(ExpectedConditions.alertIsPresent());
  }

  public String getMessage() {
    new WebDriverWait(getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"))
        .until(driver -> message.isDisplayed());
    return message.getText();
  }

  public void waitForEnabled() {
    new WebDriverWait(getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"))
        .until(ExpectedConditions.stalenessOf(enableButton.get(0)));
  }

  public void waitForDisabled() {
    new WebDriverWait(getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"))
        .until(ExpectedConditions.stalenessOf(disableButton.get(0)));
  }

  private void waitForTextAreaLoaded() {
    if (getTextAreaContent().equals("")) {
      new WebDriverWait(getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"))
          .until(driver -> !getTextAreaContent().equals(""));
    }
  }

  private String getTextAreaContent() {
    return textAreaContent.getText();
  }
}
