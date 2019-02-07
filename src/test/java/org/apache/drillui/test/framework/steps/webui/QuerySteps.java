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
import org.apache.drillui.test.framework.pages.NavigationPage;
import org.apache.drillui.test.framework.pages.QueryPage;
import org.apache.drillui.test.framework.pages.QueryResultsPage;

public final class QuerySteps {
  public enum RESULT_MODE {
    PHYSICAL,
    EXEC
  }

  public enum QUERY_TYPE {
    SQL,
    PHYSICAL,
    LOGICAL
  }

  private QuerySteps() {
  }

  public static QueryResultsPage submitQueryOnQueryPage(String query) {
    return BasePage.getPage(QueryPage.class)
        .submitQuery(query);
  }

  public static QueryResultsPage submitQueryOnQueryPage(String query, String rowsInResult) {
    return BasePage.getPage(QueryPage.class)
        .limitQueryResults(rowsInResult)
        .submitQuery(query);
  }

  public static QueryResultsPage submitQuery(String query) {
    return BasePage.getPage(NavigationPage.class)
        .navigateQuery()
        .submitQuery(query);
  }

  public static QueryResultsPage submitQuery(String query, QUERY_TYPE query_type, RESULT_MODE result_mode) {
    if(query_type == QUERY_TYPE.LOGICAL) {
      query = query.replace("\"resultMode\" : \"LOGICAL\"", "\"resultMode\" : \"" + result_mode + "\"");
    }
    return BasePage.getPage(NavigationPage.class)
        .navigateQuery()
        .changeQueryType(query_type)
        .submitQuery(query);
  }
}
