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
package testng.unsecure.query;

import org.testng.annotations.Test;
import steps.QuerySteps;
import steps.QueryResultsSteps;
import testng.unsecure.BaseUnsecureTest;

import java.util.Arrays;

import static org.testng.Assert.assertTrue;

public class ResultsTableTest extends BaseUnsecureTest {
  /*@BeforeSuite
  public void beforeSuite() {

  }

  @BeforeMethod
  public void beforeMethod() {

  }*/

  @Test(groups = { "functional" })
  public void debugTest() {
    QuerySteps.runQuery("SELECT * FROM cp.`employee.json` LIMIT 20");
    int rowsSize = 11;
    int columnsSize = 16;

    assertTrue(rowsSize == QueryResultsSteps.getResultsTableRowsCount(), "Rows count - " + QueryResultsSteps.getResultsTableRowsCount() + ", but should be - " + rowsSize);
    assertTrue(columnsSize == QueryResultsSteps.getResultsTableColumnsCount(0), "Columns count - " + QueryResultsSteps.getResultsTableColumnsCount(0) + ", but should be - " + columnsSize);
    assertTrue(QueryResultsSteps.getResultsTableRow(0).equals(Arrays.asList("employee_id", "full_name", "first_name",
        "last_name", "position_id", "position_title", "store_id", "department_id",
        "birth_date", "hire_date", "salary", "supervisor_id", "education_level",
        "marital_status", "gender", "management_role")), "Columns names on query results page are different then expected!");
  }

  /*@AfterSuite
  public void afterSuite() {
    DriverWrapper.closeBrowser();
  }*/

}
