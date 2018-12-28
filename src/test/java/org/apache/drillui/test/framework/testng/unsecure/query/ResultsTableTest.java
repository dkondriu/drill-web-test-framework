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

import org.apache.drillui.test.framework.steps.webui.NavigationSteps;
import org.apache.drillui.test.framework.testng.unsecure.BaseUnsecureTest;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import org.apache.drillui.test.framework.steps.webui.QueryResultsSteps;
import org.apache.drillui.test.framework.steps.webui.QuerySteps;
import org.apache.drillui.test.framework.steps.webui.QuerySteps.*;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class ResultsTableTest extends BaseUnsecureTest {
  @Test(groups = {"functional"})
  public void debugTest() {
    QuerySteps.submitQuery("SELECT * FROM cp.`employee.json` LIMIT 9");
    assertEquals(QueryResultsSteps.rowsCount(), 9);
    assertEquals(QueryResultsSteps.columnsCount(), 16);
    assertEquals(QueryResultsSteps.getRow(0), Arrays.asList("1", "Sheri Nowmer", "Sheri", "Nowmer", "1", "President", "0", "1", "1961-08-26", "1994-12-01 00:00:00.0", "80000.0", "0", "Graduate Degree", "S", "F", "Senior Management"));
  }

  @Test(groups = {"functional"})
  public void testSubmitPhysicalPlan() {
    QuerySteps.submitQuery("select * from cp.`employee.json` LIMIT 5");
    List<List<String>> sqlResult = QueryResultsSteps.getResultsTableBody();
    String physicalPlan = QuerySteps.submitQuery("explain plan for select * from cp.`employee.json` LIMIT 5")
            .getResultsTableBody().get(0).get(1);
    QuerySteps.submitQuery(physicalPlan, QUERY_TYPE.PHYSICAL, null);
    List<List<String>> physicalResult = QueryResultsSteps.getResultsTableBody();
    assertEquals(sqlResult.size(), physicalResult.size());
    for(int i = 0; i < sqlResult.size(); i++) {
      assertEquals(sqlResult.get(i), physicalResult.get(i));
    }
  }

  @Test(groups = {"functional"})
  public void testSubmitLogicalPlan() {
    QuerySteps.submitQuery("select * from cp.`employee.json` LIMIT 5");
    List<List<String>> sqlResult = QueryResultsSteps.getResultsTableBody();
    String logicalPlan = QuerySteps.submitQuery("explain plan without implementation for select * from cp.`employee.json` LIMIT 5")
            .getResultsTableBody().get(0).get(1);
    QuerySteps.submitQuery(logicalPlan, QUERY_TYPE.LOGICAL, RESULT_MODE.EXEC);
    List<List<String>> logicalResult = QueryResultsSteps.getResultsTableBody();
    assertEquals(sqlResult.size(), logicalResult.size());
    for(int i = 0; i < sqlResult.size(); i++) {
      assertEquals(sqlResult.get(i), logicalResult.get(i));
    }
  }

  /*@Test(groups = {"functional"})
  public void debugTest2() {
    NavigationSteps.navigateQuery();
    QuerySteps.sendQueryLine("{");
    QuerySteps.sendKeyToQuery(Keys.ENTER);
    QuerySteps.sendQueryLine("\"test param\": [some value");
    QuerySteps.sendKeyToQuery(Keys.END);
    QuerySteps.sendKeyToQuery(Keys.PAGE_DOWN);
    System.out.println();
  }*/

}
