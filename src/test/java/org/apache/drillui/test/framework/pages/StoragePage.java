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

import org.apache.drillui.test.framework.initial.PropertiesConst;
import org.apache.drillui.test.framework.initial.TestProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StoragePage extends BasePage {

  @FindBy(xpath = "//*[text()='Enabled Storage Plugins']/following-sibling::table")
  private WebElement enabledStoragePlugins;

  @FindBy(xpath = "//*[text()='Disabled Storage Plugins']/following-sibling::table")
  private WebElement disabledStoragePlugins;

  @FindBy(css = "button[data-target='#new-plugin-modal']")
  private WebElement newStoragePluginDialog;

  @FindBy(id = "configuration")
  private WebElement formTitle;

  @FindBy(css = "input[placeholder='Storage Name']")
  private WebElement newStoragePluginNameInput;

  @FindBy(xpath = "//*[@id='editor']/textarea")
  private WebElement newStoragePluginConfigInput;

  @FindBy(id = "createForm")
  private WebElement createFormButtons;

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
    new WebDriverWait(getDriver(), PropertiesConst.DEFAULT_TIMEOUT).until(ExpectedConditions.stalenessOf(button));
    return this;
  }

  public StoragePage disableStoragePlugin(String name) {
    WebElement button = getButton(getEnabledStoragePlugins().get(name), "Disable");
    button.click();
    return this;
  }

  public StoragePage waitStoragePluginToBeDisabled(String name) {
    waitForCondition(plugin -> isDisabledPluginDisplayed(name));
    return this;
  }

  public boolean isDisabledPluginDisplayed(String pluginName) {
    try {
      return getDisabledStoragePlugins().containsKey(pluginName);
    } catch (StaleElementReferenceException e) {
      return false;
    }
  }

  public StoragePage openNewStoragePluginDialog() {
    newStoragePluginDialog.click();
    return this;
  }

  public boolean formTitlePresented() {
    waitForCondition(driver -> formTitle.isDisplayed());
    return formTitle.isDisplayed() && formTitle.getText().equals("New Storage Plugin");
  }

  public boolean pluginNameInputPresented() {
    waitForCondition(driver -> newStoragePluginNameInput.isDisplayed());
    return newStoragePluginNameInput.isDisplayed();
  }

  public boolean closeButtonPresented() {
    WebElement closeButton = createFormButtons.findElement(By.cssSelector("button[type=button]"));
    waitForCondition(driver -> closeButton.isDisplayed());
    return closeButton.isDisplayed() && closeButton.isEnabled();
  }

  public boolean submitButtonPresented() {
    WebElement submitButton = createFormButtons.findElement(By.xpath("//button[text()='Create']"));
    waitForCondition(driver -> submitButton.isDisplayed());
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
    WebElement closeButton = createFormButtons.findElement(By.cssSelector("button[type=button]"));
    closeButton.click();
    waitForCondition(ExpectedConditions.invisibilityOf(closeButton));
    return this;
  }

  public StoragePage submitNewPluginForm() {
    WebElement submitButton = createFormButtons.findElement(By.xpath("//button[text()='Create']"));
    submitButton.click();
    waitForCondition(ExpectedConditions.invisibilityOf(submitButton));
    return this;
  }
}
