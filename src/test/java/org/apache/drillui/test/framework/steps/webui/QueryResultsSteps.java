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
package org.apache.drillui.test.framework.steps.webui;

import org.apache.drillui.test.framework.pages.BasePage;
import org.apache.drillui.test.framework.pages.QueryResultsPage;

import java.util.List;

public final class QueryResultsSteps extends BaseSteps {

  private static QueryResultsPage getPage() {
    return BasePage.getPage(QueryResultsPage.class);
  }

  public static int rowsCount() {
    return getPage().getResultsTable()
        .size();
  }

  public static int columnsCount() {
    return getPage().getResultsTable()
        .get(0)
        .size();
  }

  public List<String> getRow(int rowId) {
    return getPage().getResultsTable()
        .get(rowId);
  }

  public List<List<String>> getTable() {
    return getPage().getResultsTable();
  }

  public static void findInRows(String text) {
    BasePage.getPage(QueryResultsPage.class)
        .findInRows(text);
  }

  public static boolean isPaginationEnabled() {
    QueryResultsPage queryResultsPage = BasePage.getPage(QueryResultsPage.class);
    BaseSteps.setImplicitWait(0);
    boolean hasPagination = queryResultsPage.hasPrevPage() ||
        queryResultsPage.hasPaginationPages() ||
        queryResultsPage.hasNextPage();
    BaseSteps.resetImplicitWait();
    return hasPagination;
  }

  public static int getPaginationPagesCount() {
    BaseSteps.setImplicitWait(0);
    int count = BasePage.getPage(QueryResultsPage.class)
        .getPaginationPagesCount();
    BaseSteps.resetImplicitWait();
    return count;
  }

  public static void openPage(int pageNumber) {
    BaseSteps.setImplicitWait(0);
    BasePage.getPage(QueryResultsPage.class).openPage(pageNumber);
    BaseSteps.resetImplicitWait();
  }

  public String getQueryProfile() {
    return getPage().getQueryProfile();
  }

  public static boolean hasPrevPage() {
    return BasePage.getPage(QueryResultsPage.class)
        .hasPrevPage();
  }

  public static boolean hasNextPage() {
    return BasePage.getPage(QueryResultsPage.class)
        .hasNextPage();
  }

  public static String getPageRowsInfo() {
    return BasePage.getPage(QueryResultsPage.class)
        .getPageRowsInfo();
  }

  public static String getQueryStatus() {
    return BasePage.getPage(QueryResultsPage.class)
        .getQueryStatus();
  }

  public static void filterColumns(List<String> columns) {
    BasePage.getPage(QueryResultsPage.class)
        .filterColumns(columns);
  }

  public QueryResultsSteps showResultRows(String rowsCount) {
    BasePage.getPage(QueryResultsPage.class).showResultRows(rowsCount);
    return BaseSteps.getSteps(QueryResultsSteps.class);
  }
}
