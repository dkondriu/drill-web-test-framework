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

public final class QueryResultsSteps {

  private QueryResultsSteps() {
  }

  public static int rowsCount() {
    return BasePage.getPage(QueryResultsPage.class)
        .getResultsTableBody()
        .size();
  }

  public static int columnsCount() {
    return BasePage.getPage(QueryResultsPage.class)
        .getResultsTableHeader()
        .size();
  }

  public static List getRow(int rowId) {
    return BasePage.getPage(QueryResultsPage.class)
        .getResultsTableBody()
        .get(rowId);
  }

  public static List<List<String>> getResultsTableBody() {
    return BasePage.getPage(QueryResultsPage.class)
        .getResultsTableBody();
  }

  public static boolean isPaginationEnabled() {
    return BasePage.getPage(QueryResultsPage.class).hasPrevPage() ||
        BasePage.getPage(QueryResultsPage.class).hasPaginationPages() ||
        BasePage.getPage(QueryResultsPage.class).hasNextPage();
  }

  public static int getPaginationPagesCount() {
    return BasePage.getPage(QueryResultsPage.class)
        .getPaginationPagesCount();
  }

  public static void openPage(int pageNumber) {
    BasePage.getPage(QueryResultsPage.class).openPage(pageNumber);
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
}
