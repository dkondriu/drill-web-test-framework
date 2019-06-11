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
package org.apache.drill_web_test_framework.web_ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryResultsPage extends BasePage {
  @FindBy(id = "result")
  private WebElement queryResultTable;

  @FindBy(xpath = "//*[@title=\"Open in new window\"]")
  private WebElement profileButton;

  @FindBy(xpath = "/html/body/div[2]/div[2]/table/tbody/tr/td[1]/button/span[1]")
  private WebElement queryResultStatus;

  @FindBy(xpath = "//*[@id=\"result_filter\"]/label/input")
  private WebElement rowsFilter;

  @FindBy(className = "ColVis_MasterButton")
  private WebElement showHideColumns;

  @FindBy(xpath = "/html/body/ul")
  private WebElement columnsListFilter;

  @FindBy(className = "ColVis_collectionBackground")
  private WebElement outsideColumnsFilter;

  @FindBy(xpath = "//*[@id=\"result_length\"]/label/select")
  private WebElement rowsPerPage;

  @FindBy(id = "result_info")
  private WebElement pageRowsInfo;

  @FindBy(id = "result_previous")
  private WebElement previousPage;

  @FindBy(xpath = "//*[@id=\"result_paginate\"]/span")
  private WebElement pagesList;

  @FindBy(id = "result_next")
  private WebElement nextPage;

  public boolean hasPrevPage() {
    return !previousPage.getAttribute("class").endsWith("ui-state-disabled");
  }

  public boolean hasPaginationPages() {
    return pagesList.findElements(By.tagName("a")).size() > 1;
  }

  public int getPaginationPagesCount() {
    return pagesList.findElements(By.tagName("a")).size();
  }

  public void openPage(int pageNumber) {
    pagesList.findElements(By.tagName("a")).get(pageNumber - 1).click();
  }

  public boolean hasNextPage() {
    return !nextPage.getAttribute("class").endsWith("ui-state-disabled");
  }

  public String getQueryStatus() {
    return queryResultStatus.getText();
  }

  public String getPageRowsInfo() {
    return pageRowsInfo.getText();
  }

  public void findInRows(String text) {
    sendText(rowsFilter, text);
  }

  public void filterColumns(List<String> columns) {
    showHideColumns.click();
    List<WebElement> columnsElements = columnsListFilter.findElements(By.tagName("label"));
    columnsElements.forEach(column -> {
      if (columns.contains(column.findElement(By.tagName("span")).getText())) {
        showColumn(column.findElement(By.tagName("input")));
      } else {
        hideColumn(column.findElement(By.tagName("input")));
      }
    });
    outsideColumnsFilter.click();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void showColumn(WebElement column) {
    if (!column.isSelected()) {
      column.click();
    }
  }

  private void hideColumn(WebElement column) {
    if (column.isSelected()) {
      column.click();
    }
  }

  public List<List<String>> getResultsTable() {
    return getTable(queryResultTable);
  }

  public String getQueryProfile() {
    String result = profileButton.getText();
    /*
      Example:
      Button text - "Query Profile: 2393e37c-0cc6-f39a-3df1-aae892ab106c COMPLETED"
      The Pattern should return "2393e37c-0cc6-f39a-3df1-aae892ab106c"
     */
    Matcher matcher = Pattern.compile(":\\s+([\\w-]+)")
        .matcher(result);
    if (matcher.find()) {
      result = matcher.group(1);
    }
    return result;
  }

  public QueryResultsPage showResultRows(String rowsCount) {
    rowsPerPage.click();
    rowsPerPage.findElement(By.xpath("//*[text() = '" + rowsCount + "']")).click();
    if (!rowsCount.equals("ALL")) {
      waitForRowsCount((Integer.parseInt(rowsCount) + 1));
    } else {
      Matcher matcher = Pattern.compile("([\\d]+)\\w*entries")
          .matcher(pageRowsInfo.getText());
      if (matcher.find()) {
        waitForRowsCount((Integer.parseInt(matcher.group(1)) + 1));
      }
    }
    return getPage(QueryResultsPage.class);
  }

  public QueryResultsPage waitForRowsCount(int rowsCount) {
    waitForCondition(driver -> getResultsTable().size() == rowsCount);
    return getPage(QueryResultsPage.class);
  }
}
