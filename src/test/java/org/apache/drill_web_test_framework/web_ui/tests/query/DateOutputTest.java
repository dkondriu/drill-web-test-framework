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
import org.apache.drill_web_test_framework.web_ui.steps.QuerySteps;
import org.apache.drill_web_test_framework.web_ui.tests.FunctionalTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class DateOutputTest extends FunctionalTest {

  private final QuerySteps querySteps = BaseSteps.getSteps(QuerySteps.class);
  private final NavigationSteps navigationSteps=BaseSteps.getSteps(NavigationSteps.class);

  @BeforeMethod
  public void navigateQuery() {
   navigationSteps.navigateQuery();
  }

  @Test(groups = {"functional"})
  public void selectDateQuery() {
    String result = querySteps.runSQL("SELECT date '2017-04-06';")
        .getRow(1).get(0);
    String expected = "2017-04-06";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimestampZeroQuery() {
    String result = querySteps.runSQL("SELECT timestamp '2017-04-06 11:22:33';")
        .getRow(1).get(0);
    String expected = "2017-04-06T11:22:33";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimestampOneQuery() {
    String result = querySteps.runSQL("SELECT timestamp '2017-04-06 11:22:33.1';")
        .getRow(1).get(0);
    String expected = "2017-04-06T11:22:33.100";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimestampTwoQuery() {
    String result = querySteps.runSQL("SELECT timestamp '2017-04-06 11:22:33.12';")
        .getRow(1).get(0);
    String expected = "2017-04-06T11:22:33.120";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimestampThreeQuery() {
    String result = querySteps.runSQL("SELECT timestamp '2017-04-06 11:22:33.123';")
        .getRow(1).get(0);
    String expected = "2017-04-06T11:22:33.123";
    assertEquals(result, expected);
  }

  @Test(groups = {"functional"})
  public void selectTimeQuery() {
    String result = querySteps.runSQL("SELECT time '11:22:33';")
        .getRow(1).get(0);
    String expected = "11:22:33";
    assertEquals(result, expected);
  }

}
