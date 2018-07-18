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
package pages;

import initial.WebBrowser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.LinkedList;
import java.util.List;

public class QueryResultsPage extends BasePage {
  public static QueryResultsPage queryResultsPage = null;
  @FindBy(xpath = "//*[@id=\"result\"]/tbody/tr/td")
  private WebElement queryResultLine;
  @FindBy(xpath = "//*[@id=\"result_wrapper\"]/div[2]/div[1]/div/table/thead")
  private WebElement queryResultTableHead;
  @FindBy(xpath = "//*[@id=\"result\"]/tbody")
  private WebElement queryResultTableBody;

  QueryResultsPage() {
    queryResultsPage = this;
  }

  public void setQueryResultLine(WebElement queryResultLine) {
    this.queryResultLine = queryResultLine;
  }

  public WebElement getQueryResultLine() {
    return queryResultLine;
  }

  public String getFirstResultCell() {
    return queryResultLine.getText();
  }

  public List<List<String>> getResultsTable() {
    List<List<String>> rows = new LinkedList<>();
    rows.add(getResultTableHead());
    rows.addAll(getResultTableBody());
    return rows;
  }

  private List<String> getResultTableHead() {
    List<String> columns = new LinkedList<>();
    for(WebElement el : queryResultTableHead.findElements(By.tagName("div"))) {
      columns.add(el.getText());
    }
    return columns;
  }

  private List<List<String>> getResultTableBody() {
    List<List<String>> rows = new LinkedList<>();
    for(WebElement tr : queryResultTableBody.findElements(By.tagName("tr"))) {
      List<String> row = new LinkedList<>();
      for(WebElement td : tr.findElements(By.tagName("td"))) {
        row.add(td.getText());
      }
      rows.add(row);
    }
    return rows;
  }

}
