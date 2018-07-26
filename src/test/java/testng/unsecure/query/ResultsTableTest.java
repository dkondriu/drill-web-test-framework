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
import pages.BasePage;
import pages.QueryResultsPage;
import steps.AuthSteps;
import steps.QuerySteps;
import steps.QueryResultsSteps;
import testng.unsecure.BaseUnsecureTest;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class ResultsTableTest extends BaseUnsecureTest {
  @Test(groups = { "functional" })
  public void debugTest() {
    QuerySteps.runQuery("SELECT * FROM cp.`employee.json` LIMIT 9");
    assert QueryResultsSteps.rowsCount() == 9 : "Expected rows " + 9 + " but found " + QueryResultsSteps.rowsCount();
    assert QueryResultsSteps.columnsCount() == 16 : "Expected columns " + 16 + " but found " + QueryResultsSteps.columnsCount();
    assert QueryResultsSteps.rowEqualTo(0, Arrays.asList(
        "1",
        "Sheri Nowmer",
        "Sheri",
        "Nowmer",
        "1",
        "President",
        "0",
        "1",
        "1961-08-26",
        "1994-12-01 00:00:00.0",
        "80000.0",
        "0",
        "Graduate Degree",
        "S",
        "F",
        "Senior Management"
    ));
  }

}
