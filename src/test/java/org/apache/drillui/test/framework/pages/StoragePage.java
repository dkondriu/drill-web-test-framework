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

import org.apache.drillui.test.framework.steps.webui.AlertSteps;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StoragePage extends BasePage {

  // @FindBy(xpath = "//*[text()='Enabled Storage Plugins']/following-sibling::div[@class='table-responsive']")
  @FindBy(xpath = "/html/body/div[2]/div[3]")
  private WebElement enabledStoragePlugins;

  // @FindBy(xpath = "//*[text()='Disabled Storage Plugins']/following-sibling::div[@class='table-responsive']")
  @FindBy(xpath = "/html/body/div[2]/div[4]")
  private WebElement disabledStoragePlugins;

  @FindBy(css = "button[data-target='#new-plugin-modal']")
  private WebElement newStoragePluginDialog;

  @FindBy(xpath = "//*[@id=\"configuration\"]")
  private WebElement formTitle;

  @FindBy(css = "input[placeholder='Storage Name']")
  private WebElement newStoragePluginNameInput;

  @FindBy(xpath = "//*[@id=\"editor\"]/textarea")
  private WebElement newStoragePluginConfigInput;

  @FindBy(xpath = "//*[@id=\"createForm\"]/div[2]/button[1]")
  private WebElement closeButton;

  @FindBy(xpath = "//*[@id=\"createForm\"]/div[2]/button[2]")
  private WebElement submitButton;

  private Map<String, WebElement> getEnabledStoragePlugins() {
    return getStoragePlugins(enabledStoragePlugins);
  }

  private Map<String, WebElement> getDisabledStoragePlugins() {
    return getStoragePlugins(disabledStoragePlugins);
  }

  private Map<String, WebElement> getAllStoragePlugins() {
    return Stream.of(getEnabledStoragePlugins(), getDisabledStoragePlugins())
                    .flatMap(m -> m.entrySet().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private Map<String, WebElement> getStoragePlugins(WebElement storagePlugins) {
    return storagePlugins.findElements(By.tagName("tr"))
                          .stream()
                          .collect(Collectors.toMap(e -> e.findElements(By.tagName("td")).get(0).getText(),
                                                    e -> e.findElements(By.tagName("td")).get(1)));
  }

  private WebElement getButton(WebElement element, String text) {
    return element.findElement(By.xpath("button[contains(.,'" + text + "')]"));
  }

  public boolean storagePluginExists(String name) {
    return getAllStoragePlugins().containsKey(name);
  }

  public boolean storagePluginEnabled(String name) {
    return getEnabledStoragePlugins().containsKey(name);
  }

  public EditStoragePluginPage updateStoragePlugin(String name) {
    getButton(getAllStoragePlugins().get(name), "Update").click();
    return BasePage.getPage(EditStoragePluginPage.class);
  }

  public StoragePage enableStoragePlugin(String name) {
    WebElement button = getButton(getDisabledStoragePlugins().get(name), "Enable");
    button.click();
    new WebDriverWait(getDriver(), 5).until(ExpectedConditions.stalenessOf(button));
    return this;
  }

  public StoragePage disableStoragePlugin(String name) {
    WebElement button = getButton(getEnabledStoragePlugins().get(name), "Disable");
    button.click();
    new WebDriverWait(getDriver(), 5).until(ExpectedConditions.alertIsPresent());
    AlertSteps.acceptAlert();
    new WebDriverWait(getDriver(), 5).until(ExpectedConditions.stalenessOf(button));
    return this;
  }

  public StoragePage exportPlugin(String name) {
    //TODO: Add a way to manage and verify downloaded files
    return this;
  }

  public StoragePage openNewStoragePluginDialog() {
    newStoragePluginDialog.click();
    return this;
  }

  public boolean formTitlePresented() {
    new WebDriverWait(getDriver(), 5)
        .until(ExpectedConditions.visibilityOf(formTitle));
    return formTitle.isDisplayed() && formTitle.getText().equals("New Storage Plugin");
  }

  public boolean pluginNameInputPresented() {
    new WebDriverWait(getDriver(), 5)
        .until(ExpectedConditions.visibilityOf(newStoragePluginNameInput));
    return newStoragePluginNameInput.isDisplayed();
  }

  public boolean closeButtonPresented() {
    new WebDriverWait(getDriver(), 5)
        .until(ExpectedConditions.visibilityOf(closeButton));
    return closeButton.isDisplayed() && closeButton.isEnabled();
  }

  public boolean submitButtonPresented() {
    new WebDriverWait(getDriver(), 5)
        .until(ExpectedConditions.visibilityOf(submitButton));
    return submitButton.isDisplayed() && submitButton.isEnabled();
  }

  public StoragePage setNewStoragePluginName(String name) {
    sendText(newStoragePluginNameInput, name);
    return this;
  }

  public StoragePage setNewStoragePluginConfig(String storageConfig) {
    sendText(newStoragePluginConfigInput, storageConfig);
    return this;
  }

  public StoragePage closeNewPluginForm() {
    closeButton.click();
    new WebDriverWait(getDriver(), 5)
        .until(ExpectedConditions.invisibilityOf(closeButton));
    return this;
  }

  public StoragePage submitNewPluginForm() {
    submitButton.click();
    new WebDriverWait(getDriver(), 5)
        .until(ExpectedConditions.invisibilityOf(submitButton));
    return this;
  }
}