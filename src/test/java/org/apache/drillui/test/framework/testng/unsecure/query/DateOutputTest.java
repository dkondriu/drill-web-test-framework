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

import org.testng.annotations.Test;
import org.apache.drillui.test.framework.steps.webui.QueryResultsSteps;
import org.apache.drillui.test.framework.steps.webui.QuerySteps;
import org.apache.drillui.test.framework.testng.unsecure.BaseUnsecureTest;

import static org.testng.Assert.assertEquals;

public class DateOutputTest extends BaseUnsecureTest {
  @Test(groups = {"functional"})
  public void selectDateQuery() {
    QuerySteps.runQuery("select date '2017-04-06';");
    String result = (String) QueryResultsSteps.getRow(0).get(0);
    String expected = "2017-04-06";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectDateQuery2() {
    QuerySteps.runQuery("SELECT * FROM cp.`employee.json`;");
    String result = (String) QueryResultsSteps.getRow(0).get(0);
    String expected = "1";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimestampZeroQuery() {
    QuerySteps.runQuery("select timestamp '2017-04-06 11:22:33';");
    String result = (String) QueryResultsSteps.getRow(0).get(0);
    String expected = "2017-04-06 11:22:33.0";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimestampOneQuery() {
    QuerySteps.runQuery("select timestamp '2017-04-06 11:22:33.1';");
    String result = (String) QueryResultsSteps.getRow(0).get(0);
    String expected = "2017-04-06 11:22:33.1";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimestampTwoQuery() {
    QuerySteps.runQuery("select timestamp '2017-04-06 11:22:33.12';");
    String result = (String) QueryResultsSteps.getRow(0).get(0);
    String expected = "2017-04-06 11:22:33.12";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimestampThreeQuery() {
    QuerySteps.runQuery("select timestamp '2017-04-06 11:22:33.123';");
    String result = (String) QueryResultsSteps.getRow(0).get(0);
    String expected = "2017-04-06 11:22:33.123";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimeQuery() {
    QuerySteps.runQuery("select time '11:22:33';");
    String result = (String) QueryResultsSteps.getRow(0).get(0);
    String expected = "11:22:33";
    assertEquals(result, expected);
  }

}
