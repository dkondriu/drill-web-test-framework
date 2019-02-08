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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.LinkedList;
import java.util.List;

public class QueryResultsPage extends BasePage {
  @FindBy(xpath = "/html/body/div[2]/div[2]/table/tbody/tr/td[1]/button")
  private WebElement profileButton;

  @FindBy(xpath = "/html/body/div[2]/div[2]/table/tbody/tr/td[1]/button/span[1]")
  private WebElement queryResultStatus;

  @FindBy(id = "delimitBy")
  private WebElement exportCSVDelimiter;

  @FindBy(xpath = "/html/body/div[2]/div[2]/table/tbody/tr/td[3]/button")
  private WebElement exportButton;

  @FindBy(xpath = "//*[@id=\"result_filter\"]/label/input")
  private WebElement rowsFilter;

  @FindBy(xpath = "//*[@id=\"result_wrapper\"]/div[1]/div[2]/button")
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

  @FindBy(xpath = "//*[@id=\"result\"]/tbody/tr/td")
  private WebElement queryResultLine;

  @FindBy(xpath = "//*[@id=\"result_wrapper\"]/div[2]/div[1]/div/table/thead")
  private WebElement queryResultTableHeader;

  @FindBy(xpath = "//*[@id=\"result\"]/tbody")
  private WebElement queryResultTableBody;

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

  public List<String> getResultsTableHeader() {
    List<String> resultsTableHead = new LinkedList<>();
    for (String column : queryResultTableHeader.getAttribute("outerHTML").split("<th")) {
      if (!column.contains("</th>")) {
        continue;
      }
      resultsTableHead.add(column.
          replaceAll("<span .*", "").
          replaceAll(".*class=\"DataTables_sort_wrapper\">", "").
          replaceAll("</thead>", "").trim());
    }
    return resultsTableHead;
  }

  public List<List<String>> getResultsTableBody() {
    List<List<String>> resultsTable = new LinkedList<>();
    for (String row : queryResultTableBody.getAttribute("outerHTML").split("<tr")) {
      if (!row.contains("</tr>")) {
        continue;
      }
      List<String> columns = new LinkedList<>();
      for (String column : row.replaceAll("</tr>.*", "").trim().split("</td>")) {
        columns.add(column.replaceAll(".*>", "").trim());
      }
      resultsTable.add(columns);
    }
    return resultsTable;
  }

  public String getFirstResultCell() {
    return queryResultLine.getText();
  }

  public void findInRows(String text) {
    rowsFilter.clear();
    rowsFilter.sendKeys(text);
  }

  public void filterColumns(List<String> columns) {
    showHideColumns.click();
    List<WebElement> columnsElements = new LinkedList<>();
    columnsElements.addAll(columnsListFilter.findElements(By.tagName("label")));
    for(WebElement col: columnsElements) {
      if(columns.contains(col.findElement(By.tagName("span")).getText())) {
        showColumn(col.findElement(By.tagName("input")));
      } else {
        hideColumn(col.findElement(By.tagName("input")));
      }
    }
    outsideColumnsFilter.click();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    /*try {
      WebDriverWait wait = new WebDriverWait(WebBrowser.getDriver(), TestProperties.getInt("DEFAULT_TIMEOUT"));
      wait.until(ExpectedConditions.stalenessOf(outsideColumnsFilter));
    } catch (Exception e) {
      e.printStackTrace();
    }*/
  }

  public String getQueryProfileId() {
    return profileButton.getText().replace("Query Profile: ", "").replaceAll(" .+", "");
  }

  public void exportCSV(String delimiter) {
    exportCSVDelimiter.clear();
    exportCSVDelimiter.sendKeys(delimiter);
    exportButton.click();
  }

  public void exportCSV() {
    exportButton.click();
  }

  private void showColumn(WebElement column) {
    if(!column.isSelected()) {
      column.click();
    }
  }

  private void hideColumn(WebElement column) {
    if(column.isSelected()) {
      column.click();
    }
  }
}
