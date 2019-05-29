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
package org.apache.drillui.test.framework.testng.unsecure.query;

import org.apache.drillui.test.framework.steps.webui.BaseSteps;
import org.apache.drillui.test.framework.steps.webui.NavigationSteps;
import org.apache.drillui.test.framework.steps.webui.QueryExceptionsSteps;
import org.apache.drillui.test.framework.steps.webui.QueryResultsSteps;
import org.apache.drillui.test.framework.steps.webui.QuerySteps;
import org.apache.drillui.test.framework.testng.unsecure.BaseUnsecureTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ResultsExceptionsTest extends BaseUnsecureTest {
  private final QuerySteps querySteps = BaseSteps.getSteps(QuerySteps.class);
  private final QueryResultsSteps queryResultsSteps = BaseSteps.getSteps(QueryResultsSteps.class);
  private final NavigationSteps navigationSteps = BaseSteps.getSteps(NavigationSteps.class);

  @Test(groups = {"functional"})
  public void queryWithException() {
    navigationSteps.navigateQuery()
        .runSQL("ELECT * FROM cp.`employee.json` LIMIT 9");
    assertTrue(QueryExceptionsSteps.hasException());
    assertTrue(QueryExceptionsSteps.getFullStackTrace().contains("org.apache.drill.common.exceptions.UserRemoteException: PARSE ERROR: Non-query expression encountered in illegal context"));
  }

  @Test(groups = {"functional"})
  public void backFromException() {
    navigationSteps.navigateQuery()
        .runSQL("SELECT FROM cp.`employee.json` LIMIT 9");
    assertTrue(QueryExceptionsSteps.hasException());
    assertTrue(QueryExceptionsSteps.getFullStackTrace().contains("org.apache.drill.common.exceptions.UserRemoteException: PARSE ERROR: Encountered \"FROM\" at line 1, column 8."));
    QueryExceptionsSteps.goBack();
    querySteps.runSQL("SELECT * FROM cp.`employee.json` LIMIT 9");
    assertEquals(queryResultsSteps.getRow(1).get(2), "Sheri");
  }

}
