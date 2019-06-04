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
package org.apache.drill_web_test_framework.web_ui.tests.query;

import org.apache.drill_web_test_framework.web_ui.steps.BaseSteps;
import org.apache.drill_web_test_framework.web_ui.steps.NavigationSteps;
import org.apache.drill_web_test_framework.web_ui.tests.FunctionalTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.apache.drill_web_test_framework.web_ui.steps.QueryResultsSteps;
import org.apache.drill_web_test_framework.web_ui.steps.QuerySteps;

import java.util.Arrays;
import java.util.List;

import static org.apache.drill_web_test_framework.web_ui.steps.QuerySteps.RESULT_MODE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ResultsTableTest extends FunctionalTest {

  private final QuerySteps querySteps = BaseSteps.getSteps(QuerySteps.class);
  private final QueryResultsSteps queryResultsSteps = BaseSteps.getSteps(QueryResultsSteps.class);
  private final NavigationSteps navigationSteps = BaseSteps.getSteps(NavigationSteps.class);

  @BeforeMethod
  public void navigateQuery() {
    navigationSteps.navigateQuery();
  }

  @Test(groups = {"functional"})
  public void testSubmitQuery() {
    querySteps.runSQL("SELECT * FROM cp.`employee.json` LIMIT 9");

    assertEquals(queryResultsSteps.rowsCount(), 10);
    assertEquals(queryResultsSteps.columnsCount(), 16);
    assertEquals(queryResultsSteps.getRow(1), Arrays.asList("1", "Sheri Nowmer", "Sheri", "Nowmer", "1", "President", "0", "1", "1961-08-26", "1994-12-01 00:00:00.0", "80000.0", "0", "Graduate Degree", "S", "F", "Senior Management"));
    assertFalse(queryResultsSteps.isPaginationEnabled());
    assertEquals(queryResultsSteps.getQueryStatus(), "COMPLETED");
  }

  @Test(groups = {"functional"})
  public void testSubmitPhysicalPlan() {
    List<List<String>> sqlResult = querySteps.runSQL("select * from cp.`employee.json` LIMIT 5")
        .getTable();
    String physicalPlan = navigationSteps.navigateQuery()
        .runSQL("explain plan for select * from cp.`employee.json` LIMIT 5")
        .getRow(1)
        .get(1);
    navigationSteps.navigateQuery()
        .runPhysical(physicalPlan);
    assertEquals(sqlResult.size(), queryResultsSteps.rowsCount());
    for (int i = 0; i < sqlResult.size(); i++) {
      assertEquals(sqlResult.get(i), queryResultsSteps.getRow(i));
    }
  }

  @Test(groups = {"functional"})
  public void testSubmitLogicalPlan() {
    List<List<String>> sqlResult = querySteps.runSQL("select * from cp.`employee.json` LIMIT 5")
        .getTable();
    String logicalPlan = navigationSteps.navigateQuery()
        .runSQL("explain plan without implementation for select * from cp.`employee.json` LIMIT 5")
        .getRow(1)
        .get(1);
    navigationSteps.navigateQuery()
        .runLogical(QuerySteps.prepareLogicalPlan(logicalPlan, RESULT_MODE.EXEC));
    assertEquals(sqlResult.size(), queryResultsSteps.rowsCount());
    for (int i = 0; i < sqlResult.size(); i++) {
      assertEquals(sqlResult.get(i), queryResultsSteps.getRow(i));
    }
  }

  @Test(groups = {"functional"})
  public void testPagination() {
    querySteps.runSQL("select * from cp.`employee.json` LIMIT 33");
    assertFalse(queryResultsSteps.hasPrevPage());
    assertTrue(queryResultsSteps.hasNextPage());
    assertEquals(queryResultsSteps.getPageRowsInfo(), "Showing 1 to 10 of 33 entries");
    assertEquals(queryResultsSteps.getPaginationPagesCount(), 4);
    queryResultsSteps.openPage(2);
    assertTrue(queryResultsSteps.hasPrevPage());
    assertTrue(queryResultsSteps.hasNextPage());
    assertEquals(queryResultsSteps.getPageRowsInfo(), "Showing 11 to 20 of 33 entries");
    queryResultsSteps.openPage(4);
    assertTrue(queryResultsSteps.hasPrevPage());
    assertFalse(queryResultsSteps.hasNextPage());
    assertEquals(queryResultsSteps.getPageRowsInfo(), "Showing 31 to 33 of 33 entries");
    queryResultsSteps.openPage(1);
    assertFalse(queryResultsSteps.hasPrevPage());
    assertTrue(queryResultsSteps.hasNextPage());
    assertEquals(queryResultsSteps.getPageRowsInfo(), "Showing 1 to 10 of 33 entries");
  }

  // @Test(groups = {"functional"}) // Disabled due to DRILL-6960
  public void testLimitResults() {
    querySteps.runLimitedSQL("select * from cp.`employee.json` LIMIT 33", "13");
    assertEquals(queryResultsSteps.getPaginationPagesCount(), 2);
    assertEquals(queryResultsSteps.getPageRowsInfo(), "Showing 1 to 10 of 13 entries");
  }

  @Test(groups = {"functional"})
  public void testColumnsFilter() {
    querySteps.runSQL("select * from cp.`employee.json` LIMIT 3");
    queryResultsSteps.filterColumns(Arrays.asList("employee_id", "full_name"));
    assertEquals(queryResultsSteps.getRow(1).size(), 2);
    assertEquals(queryResultsSteps.getRow(1).get(0), "1");
    assertEquals(queryResultsSteps.getRow(1).get(1), "Sheri Nowmer");
  }

  @Test(groups = {"functional"})
  public void testRowsFilter() {
    querySteps.runSQL("select * from cp.`employee.json` LIMIT 3");
    queryResultsSteps.findInRows("Nowmer");
    assertEquals(queryResultsSteps.getTable().size(), 2);
    assertEquals(queryResultsSteps.getRow(1).get(3), "Nowmer");
    assertEquals(queryResultsSteps.getPageRowsInfo(), "Showing 1 to 1 of 1 entries (filtered from 3 total entries)");
  }

  @Test(groups = {"functional"})
  public void testRowsOutputCounter() {
    querySteps.runSQL("select * from cp.`employee.json` LIMIT 3");
    queryResultsSteps.findInRows("Nowmer");
    assertEquals(queryResultsSteps.getTable().size(), 2);
    assertEquals(queryResultsSteps.getRow(1).get(3), "Nowmer");
    assertEquals(queryResultsSteps.getPageRowsInfo(), "Showing 1 to 1 of 1 entries (filtered from 3 total entries)");
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
