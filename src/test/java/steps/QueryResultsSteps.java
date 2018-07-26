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
package steps;

import pages.BasePage;
import pages.QueryResultsPage;

import java.util.Arrays;
import java.util.List;

public interface QueryResultsSteps {
  static int rowsCount() {
    return BasePage.getPage(QueryResultsPage.class)
        .getResultsTableBody()
        .size();
  }
  static int columnsCount() {
    return BasePage.getPage(QueryResultsPage.class)
        .getResultsTableHeader()
        .size();
  }
  static boolean rowEqualTo(int rowId, List<String> rowToCompare) {
    return BasePage.getPage(QueryResultsPage.class)
        .getResultsTableBody()
        .get(rowId)
        .equals(rowToCompare);
  }
}
