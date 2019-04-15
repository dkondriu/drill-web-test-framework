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

import com.google.common.base.Stopwatch;
import org.apache.drillui.test.framework.initial.TestProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QueryProfileDetailsPage extends BasePage {
  @FindBy(id = "query-tabs")
  private WebElement tabs;

  @FindBy(id = "query-content")
  private WebElement panels;

  @FindBy(id = "sql")
  private WebElement radioSQL;

  @FindBy(id = "physical")
  private WebElement radioPhysical;

  @FindBy(id = "logical")
  private WebElement radioLogical;

  @FindBy(xpath = "//button[contains(text(), 'Re-run query')]")
  private WebElement rerunButton;

  @FindBy(xpath = "//h3[contains(text(), 'Query Profile:')]")
  private WebElement profileLabel;

  @FindBy(id = "query-profile-overview")
  private WebElement profileOverview;

  @FindBy(id = "query-profile-duration")
  private WebElement profileDuration;

  @FindBy(id = "fragment-overview")
  private WebElement fragmentOverview;

  @FindBy(xpath = "//id[contains(text(), 'fragment-')]")
  private List<WebElement> fragments;

  @FindBy(id = "operator-overview")
  private WebElement operatorOverview;

  @FindBy(xpath = "//id[contains(text(), 'operator-')]")
  private List<WebElement> operators;

  @FindBy(id = "query-visual-canvas")
  private WebElement printedNodes;

  @FindBy(xpath = "//button[contains(text(), 'Print Plan')]")
  private WebElement printPlan;

  @FindBy(id = "operator-accordion")
  private WebElement operatorAccordion;

  @FindBy(linkText = "JSON profile")
  private WebElement jsonProfileButton;

  @FindBy(id = "full-json-profile-json")
  private WebElement fullJSONProfile;

  @FindBy(xpath = "//a[@data-toggle='collapse']")
  private List<WebElement> collapsibleItems;

  private WebElement activePanel() {
    return panels.findElement(By.cssSelector(".active"));
  }

  private WebElement queryTextArea() {
    return activePanel().findElement(By.className("ace_text-input"));
  }

  private WebElement queryContent() {
    return activePanel().findElement(By.className("ace_content"));
  }

  public String getProfileLabelText() {
    String result = profileLabel.getText();
    /*
      Example:
      Label text - "Query Profile: 2393e37c-0cc6-f39a-3df1-aae892ab106c"
      The Pattern should return "2393e37c-0cc6-f39a-3df1-aae892ab106c"
     */
    Matcher matcher = Pattern.compile(":\\s+([\\w-]+)")
        .matcher(result);
    if (matcher.find()) {
      result = matcher.group(1);
    }
    return result;
  }

  public QueryProfileDetailsPage navigateTab(String tabText) {
    forceClick(tabs.findElement(By.linkText(tabText)));
    return this;
  }

  public String activeTab() {
    return tabs.findElement(By.className("active"))
        .getText();
  }

  public List<Map<String, String>> getPlanNodes() {
    return getPlanNodes(activePanel());
  }

  public List<Map<String, String>> getPrintedPlanNodes() {
    return getPlanNodes(printedNodes);
  }

  private List<Map<String, String>> getPlanNodes(WebElement parentElement) {
    return parentElement.findElements(By.cssSelector(".node.enter"))
        .stream()
        .map(element -> {
          Map<String, String> node = new HashMap<>();
          node.put("label", element.getText());
          String coordinate = element.getAttribute("transform");
          Matcher matcher = Pattern.compile(",(\\d+)\\)")
              .matcher(coordinate);
          if (matcher.find()) {
            coordinate = matcher.group(1);
          }
          node.put("coordinate", coordinate);
          return node;
        }).collect(Collectors.toList());
  }

  public String activePanelId() {
    // Cannot use By.className due to a space in the class name.
    return activePanel().getAttribute("id");
  }

  public String getQueryText() {
    return queryContent().getText();
  }

  public String getPlan() {
    return activePanel().getText();
  }

  public QueryProfileDetailsPage setQueryText(String text) {
    sendText(queryTextArea(), text);
    return this;
  }

  public List<List<String>> getProfileOverview() {
    return getTable(profileOverview);
  }

  public List<List<String>> getProfileDuration() {
    return getTable(profileDuration);
  }

  public List<List<String>> getFragmentOverview() {
    return getTable(fragmentOverview);
  }

  public List<List<List<String>>> getFragments() {
    return fragments.stream()
        .map(this::getTable)
        .collect(Collectors.toList());
  }

  public List<List<String>> getOperatorOverview() {
    return getTable(operatorOverview);
  }

  public List<List<List<String>>> getOperators() {
    return operators.stream()
        .map(this::getTable)
        .collect(Collectors.toList());
  }

  public List<String> getOperatorsList() {
    return operatorAccordion.findElements(By.className("panel-heading")).stream()
        .skip(1)
        .map(WebElement::getText)
        .filter(text -> !text.equals(""))
        .collect(Collectors.toList());
  }

  public void clickAllCollapsibleItems() {
    collapsibleItems.forEach(this::clickOnCollapsibleItem);
  }

  public void collapseAllItemsReversed() {
    Iterator<WebElement> iterator = new LinkedList<>(collapsibleItems).descendingIterator();
    while (iterator.hasNext()) {
      WebElement item = iterator.next();
      clickOnCollapsibleItem(item);
      waitForCondition(driver -> item.getAttribute("class").contains("collapsed"),
          () -> clickOnCollapsibleItem(item));
    }
  }

  private void clickOnCollapsibleItem(WebElement item) {
    waitForCondition(driver -> isElementStable(item));
    forceClick(item);
    waitForCondition(driver -> isElementStable(item));
  }

  private void forceClick(WebElement element) {
    Actions actions = new Actions(getDriver());
    Stopwatch stopwatch = Stopwatch.createStarted();
    int timeStep = 500;
    boolean success = false;
    while (stopwatch.elapsed(TimeUnit.SECONDS) < TestProperties.getInt("DEFAULT_TIMEOUT")) {
      try {
        element.click();
        success = true;
        break;
      } catch (Exception e) {
        actions.sendKeys(Keys.PAGE_UP)
            .perform();
        try {
          Thread.sleep(timeStep);
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
      }
    }
    if (!success) {
      element.click();
    }
  }

  public void expandAllCollapsedItems() {
    collapsibleItems.stream()
        .filter(WebElement::isDisplayed)
        .filter(element ->
            element.getAttribute("class").contains("collapsed"))
        .forEach(this::clickOnCollapsibleItem);
  }

  public boolean isAllItemsCollapsed() {
    return listOfCollapsedElements(collapsibleItems).equals(collapsibleItems);
  }

  public boolean isAllItemsExpanded() {
    return listOfCollapsedElements(collapsibleItems).isEmpty();
  }

  private List<WebElement> listOfCollapsedElements(List<WebElement> inputList) {
    return inputList.stream()
        .filter(element ->
            element.getAttribute("class").contains("collapsed"))
        .collect(Collectors.toList());
  }

  public QueryProfileDetailsPage expandProfile() {
    forceClick(jsonProfileButton);
    return this;
  }

  public String getFullJSONProfile() {
    return fullJSONProfile.getText();
  }

  public QueryProfileDetailsPage waitForQueryText() {
    waitForCondition(driver -> !getQueryText().equals(""));
    return this;
  }

  public QueryProfileDetailsPage waitForJSONText() {
    waitForCondition(driver ->
            !fullJSONProfile.getText()
                .trim().equals(""),
        this::expandProfile);
    return this;
  }

  public QueryProfileDetailsPage setQueryType(QueryType queryType) {
    switch (queryType) {
      case SQL:
        radioSQL.click();
        break;
      case PHYSICAL:
        radioPhysical.click();
        break;
      case LOGICAL:
        radioLogical.click();
        break;
    }
    return this;
  }

  public void rerunQuery() {
    rerunButton.submit();
  }

  public void refresh() {
    getDriver().navigate().refresh();
  }

  public Thread printPlan() {
    Thread t = new Thread(() -> printPlan.click());
    t.start();
    return t;
  }

  public enum QueryType {
    SQL,
    PHYSICAL,
    LOGICAL
  }
}
