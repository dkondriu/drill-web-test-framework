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
package org.apache.drill_web_test_framework.web_ui.steps;

import org.apache.drill_web_test_framework.web_ui.pages.BasePage;
import org.apache.drill_web_test_framework.web_ui.pages.QueryPage;
import org.apache.drill_web_test_framework.web_ui.pages.QueryPage.QueryType;

public final class QuerySteps extends BaseSteps {
  public enum RESULT_MODE {
    PHYSICAL,
    EXEC
  }

  public QueryResultsSteps runLimitedSQL(String queryText, String resultRowsCount) {
    getQueryPage()
        .setQueryType(QueryType.SQL)
        .limitQueryResults(resultRowsCount)
        .submitQuery(queryText);
    return getSteps(QueryResultsSteps.class);
  }

  public QueryResultsSteps runSQL(String queryText) {
    getQueryPage()
        .setQueryType(QueryType.SQL)
        .submitQuery(queryText);
    return getSteps(QueryResultsSteps.class);
  }

  public QueryResultsSteps runPhysical(String queryText) {
    getQueryPage()
        .setQueryType(QueryType.PHYSICAL)
        .submitQuery(queryText);
    return getSteps(QueryResultsSteps.class);
  }

  public QueryResultsSteps runLogical(String queryText) {
    getQueryPage()
        .setQueryType(QueryType.LOGICAL)
        .submitQuery(queryText);
    return getSteps(QueryResultsSteps.class);
  }

  public static String prepareLogicalPlan(String queryText, RESULT_MODE result_mode) {
    return queryText.replace("\"resultMode\" : \"LOGICAL\"", "\"resultMode\" : \"" + result_mode + "\"");
  }

  public QueryResultsSteps explainPlanForQuery(String queryText) {
    return runSQL("explain plan for " + queryText);
  }

  public QueryResultsSteps explainPlanLogicalForQuery(String queryText) {
    return runSQL("explain plan without implementation for " + queryText);
  }

  private QueryPage getQueryPage() {
    return BasePage.getPage(QueryPage.class);
  }
}
