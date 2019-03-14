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
import org.apache.drillui.test.framework.testng.unsecure.BaseUnsecureTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.drillui.test.framework.steps.webui.QueryResultsSteps;
import org.apache.drillui.test.framework.steps.webui.QuerySteps;

import java.util.Arrays;
import java.util.List;

import static org.apache.drillui.test.framework.steps.webui.QuerySteps.RESULT_MODE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ResultsTableTest extends BaseUnsecureTest {

  private final QuerySteps querySteps = BaseSteps.getSteps(QuerySteps.class);
  private final QueryResultsSteps queryResultsSteps = BaseSteps.getSteps(QueryResultsSteps.class);

  @BeforeMethod
  public void navigateQuery() {
    NavigationSteps.navigateQuery();
  }

  @Test(groups = {"functional"})
  public void testSubmitQuery() {
    querySteps.runSQL("SELECT * FROM cp.`employee.json` LIMIT 9");
    assertEquals(QueryResultsSteps.rowsCount(), 10);
    assertEquals(QueryResultsSteps.columnsCount(), 16);
    assertEquals(queryResultsSteps.getRow(1), Arrays.asList("1", "Sheri Nowmer", "Sheri", "Nowmer", "1", "President", "0", "1", "1961-08-26", "1994-12-01 00:00:00.0", "80000.0", "0", "Graduate Degree", "S", "F", "Senior Management"));
    assertFalse(QueryResultsSteps.isPaginationEnabled());
    assertEquals(QueryResultsSteps.getQueryStatus(), "COMPLETED");
  }

  @Test(groups = {"functional"})
  public void testSubmitPhysicalPlan() {
    List<List<String>> sqlResult = querySteps.runSQL("select * from cp.`employee.json` LIMIT 5")
        .getTable();
    String physicalPlan = NavigationSteps.navigateQuery()
        .runSQL("explain plan for select * from cp.`employee.json` LIMIT 5")
        .getRow(1)
        .get(1);
    NavigationSteps.navigateQuery()
        .runPhysical(physicalPlan);
    assertEquals(sqlResult.size(), QueryResultsSteps.rowsCount());
    for (int i = 0; i < sqlResult.size(); i++) {
      assertEquals(sqlResult.get(i), queryResultsSteps.getRow(i));
    }
  }

  @Test(groups = {"functional"})
  public void testSubmitLogicalPlan() {
    List<List<String>> sqlResult = querySteps.runSQL("select * from cp.`employee.json` LIMIT 5")
        .getTable();
    String logicalPlan = NavigationSteps.navigateQuery()
        .runSQL("explain plan without implementation for select * from cp.`employee.json` LIMIT 5")
        .getRow(1)
        .get(1);
    NavigationSteps.navigateQuery()
        .runLogical(QuerySteps.prepareLogicalPlan(logicalPlan, RESULT_MODE.EXEC));
    assertEquals(sqlResult.size(), QueryResultsSteps.rowsCount());
    for (int i = 0; i < sqlResult.size(); i++) {
      assertEquals(sqlResult.get(i), queryResultsSteps.getRow(i));
    }
  }

  @Test(groups = {"functional"})
  public void testPagination() {
    querySteps.runSQL("select * from cp.`employee.json` LIMIT 33");
    assertFalse(QueryResultsSteps.hasPrevPage());
    assertTrue(QueryResultsSteps.hasNextPage());
    assertEquals(QueryResultsSteps.getPageRowsInfo(), "Showing 1 to 10 of 33 entries");
    assertEquals(QueryResultsSteps.getPaginationPagesCount(), 4);
    QueryResultsSteps.openPage(2);
    assertTrue(QueryResultsSteps.hasPrevPage());
    assertTrue(QueryResultsSteps.hasNextPage());
    assertEquals(QueryResultsSteps.getPageRowsInfo(), "Showing 11 to 20 of 33 entries");
    QueryResultsSteps.openPage(4);
    assertTrue(QueryResultsSteps.hasPrevPage());
    assertFalse(QueryResultsSteps.hasNextPage());
    assertEquals(QueryResultsSteps.getPageRowsInfo(), "Showing 31 to 33 of 33 entries");
    QueryResultsSteps.openPage(1);
    assertFalse(QueryResultsSteps.hasPrevPage());
    assertTrue(QueryResultsSteps.hasNextPage());
    assertEquals(QueryResultsSteps.getPageRowsInfo(), "Showing 1 to 10 of 33 entries");
  }

  // @Test(groups = {"functional"}) // Disabled due to DRILL-6960
  public void testLimitResults() {
    querySteps.runLimitedSQL("select * from cp.`employee.json` LIMIT 33", "13");
    assertEquals(QueryResultsSteps.getPaginationPagesCount(), 2);
    assertEquals(QueryResultsSteps.getPageRowsInfo(), "Showing 1 to 10 of 13 entries");
  }

  @Test(groups = {"functional"})
  public void testColumnsFilter() {
    querySteps.runSQL("select * from cp.`employee.json` LIMIT 3");
    QueryResultsSteps.filterColumns(Arrays.asList("employee_id", "full_name"));
    assertEquals(queryResultsSteps.getRow(1).size(), 2);
    assertEquals(queryResultsSteps.getRow(1).get(0), "1");
    assertEquals(queryResultsSteps.getRow(1).get(1), "Sheri Nowmer");
  }

  @Test(groups = {"functional"})
  public void testRowsFilter() {
    querySteps.runSQL("select * from cp.`employee.json` LIMIT 3");
    QueryResultsSteps.findInRows("Nowmer");
    assertEquals(queryResultsSteps.getTable().size(), 2);
    assertEquals(queryResultsSteps.getRow(1).get(3), "Nowmer");
    assertEquals(QueryResultsSteps.getPageRowsInfo(), "Showing 1 to 1 of 1 entries (filtered from 3 total entries)");
  }

  @Test(groups = {"functional"})
  public void testRowsOutputCounter() {
    querySteps.runSQL("select * from cp.`employee.json` LIMIT 3");
    QueryResultsSteps.findInRows("Nowmer");
    assertEquals(queryResultsSteps.getTable().size(), 2);
    assertEquals(queryResultsSteps.getRow(1).get(3), "Nowmer");
    assertEquals(QueryResultsSteps.getPageRowsInfo(), "Showing 1 to 1 of 1 entries (filtered from 3 total entries)");
  }

  @Test(groups = {"functional"})
  public void testShowEntries() {
    assertEquals(querySteps.runSQL("select * from cp.`employee.json` LIMIT 150").getTable().size(), 11);
    assertEquals(queryResultsSteps.showResultRows("25").getTable().size(), 26);
    assertEquals(queryResultsSteps.showResultRows("50").getTable().size(), 51);
    assertEquals(queryResultsSteps.showResultRows("75").getTable().size(), 76);
    assertEquals(queryResultsSteps.showResultRows("100").getTable().size(), 101);
    assertEquals(queryResultsSteps.showResultRows("ALL").getTable().size(), 151);
    assertEquals(queryResultsSteps.showResultRows("10").getTable().size(), 11);
  }
}
