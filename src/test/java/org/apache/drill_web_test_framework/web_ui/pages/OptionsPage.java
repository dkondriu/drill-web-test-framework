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
package org.apache.drill_web_test_framework.web_ui.pages;

import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.apache.drill_web_test_framework.web_ui.WebBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

public class OptionsPage extends BasePage {

  @FindBy(id = "searchBox")
  private WebElement searchField;

  @FindBy(css = "button[title='Clear search']")
  private WebElement clearSearchButton;

  @FindBy(id = "optionsTbl")
  private WebElement optionTable;

  @FindBy(xpath = "//th[text()='OPTION']")
  private WebElement optionSortBy;

  private void fluentWait(WebElement element, By childLocator) {
    FluentWait<WebDriver> fluentWait = new FluentWait<>(WebBrowser.getDriver())
        .withTimeout(Duration.ofSeconds(PropertiesConst.DEFAULT_TIMEOUT))
        .pollingEvery(Duration.ofMillis(200))
        .ignoring(StaleElementReferenceException.class);
    fluentWait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(element, childLocator));
  }

  private void fluentWaitForPageRefresh(WebElement element) {
    FluentWait<WebDriver> fluentWait = new FluentWait<>(getDriver())
        .withTimeout(Duration.ofSeconds(PropertiesConst.DEFAULT_TIMEOUT))
        .pollingEvery(Duration.ofMillis(200))
        .ignoring(StaleElementReferenceException.class);
    fluentWait.until(ExpectedConditions.invisibilityOf(element));
  }

  private WebElement findOption(String optionName) {
    return getDriver().findElement(By.id((optionName)));
  }

  private WebElement getInputFieldOrDropDown(String optionName) {
    return findOption(optionName).findElement((By.name("value")));
  }

  private WebElement getUpdateButton(String optionName) {
    return findOption(optionName).findElement(By.cssSelector("button[type='button']:nth-child(1)"));
  }

  private WebElement getDefaultButton(String optionName) {
    return findOption(optionName).findElement(By.cssSelector("button[type='button']:nth-child(2)"));
  }

  private WebElement getCurrentOptionValue(String optionName, String value) {
    return findOption(optionName).findElement(By.cssSelector("input[value='" + value + "']"));
  }

  private WebElement getBooleanCheckedValue(String optionName, String trueOrFalse) {
    return findOption(optionName).findElement(By.cssSelector("option[value='" + trueOrFalse + "']:checked"));
  }

  private WebElement getBooleanTrueOrFalse(String optionName, String trueOrFalse) {
    return findOption(optionName).findElement(By.cssSelector("option[value='" + trueOrFalse + "']"));
  }

  public OptionsPage setOptionToDefault(String optionName) {
    WebElement defaultButton = getDefaultButton(optionName);
    defaultButton.click();
    fluentWaitForPageRefresh(defaultButton);
    return getPage(OptionsPage.class);
  }

  public OptionsPage updateBooleanTrueOrFalse(String optionName, String trueOrFalse) {
    WebElement updateButton = getUpdateButton(optionName);
    getInputFieldOrDropDown(optionName).click();
    getBooleanTrueOrFalse(optionName, trueOrFalse).click();
    updateButton.click();
    fluentWaitForPageRefresh(updateButton);
    return getPage(OptionsPage.class);
  }

  public OptionsPage updateFieldOption(String optionName, String value) {
    WebElement updateButton = getUpdateButton(optionName);
    sendText(getInputFieldOrDropDown(optionName), value);
    updateButton.click();
    fluentWaitForPageRefresh(updateButton);
    return getPage(OptionsPage.class);
  }

  public boolean isValueAppropriate(String optionName, String value) {
    fluentWait(findOption(optionName), By.cssSelector("input[value='" + value + "']"));
    return getCurrentOptionValue(optionName, value).isDisplayed();
  }

  public boolean isBooleanValueAppropriate(String optionName, String trueOrFalse) {
    fluentWait(findOption(optionName), By.cssSelector("option[value='" + trueOrFalse + "']:checked"));
    return getBooleanCheckedValue(optionName, trueOrFalse).isSelected();
  }

  public List<List<String>> getOptionsTable() {
    return getTable(optionTable);
  }

  public OptionsPage searchFieldSendKeys(String string) {
    sendText(searchField, string);
    return getPage(OptionsPage.class);
  }

  public OptionsPage clearSearchField() {
    clearSearchButton.click();
    return getPage(OptionsPage.class);
  }

  public OptionsPage selectQuickFilter(String filter) {
    getDriver().findElement(By.xpath("//button[text()='" + filter + "']")).click();
    return getPage(OptionsPage.class);
  }

  public OptionsPage sortByOption() {
    optionSortBy.click();
    return getPage(OptionsPage.class);
  }

  public boolean isSearchFieldFilled(String string) {
    String fieldContent = searchField.getAttribute("value");
    return fieldContent.equals(string);
  }
}
