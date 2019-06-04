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
import org.apache.drill_web_test_framework.web_ui.steps.ErrorSteps;
import org.apache.drill_web_test_framework.web_ui.steps.NavigationSteps;
import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.testng.annotations.Test;
import org.apache.drill_web_test_framework.web_ui.steps.QuerySteps;
import org.apache.drill_web_test_framework.web_ui.tests.FunctionalTest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ErrorTest extends FunctionalTest {

  private final QuerySteps querySteps = BaseSteps.getSteps(QuerySteps.class);
  private final NavigationSteps navigationSteps = BaseSteps.getSteps(NavigationSteps.class);

  @Test(groups = {"functional"})
  public void queryWithError() {
    BaseSteps.openUrl("/djmfhhgkdjs");
    String error = ErrorSteps.getFullStackTrace();
    assertFalse(error.isEmpty());
    switch (PropertiesConst.DRIVER_TYPE) {
      case "FIREFOX":
        assertEquals(error, "errorMessage \"HTTP 404 Not Found\"");
        break;
      case "CHROME":
        assertEquals(error, "{\n  \"errorMessage\" : \"HTTP 404 Not Found\"\n}");
        break;
      case "EDGE":
        assertEquals(error, "{\n  \"errorMessage\" : \"HTTP 404 Not Found\"\n}");
        break;
    }
    BaseSteps.openUrl("/");
  }

  @Test(groups = {"functional"})
  public void queryWithoutError() {
    navigationSteps.navigateQuery()
        .runSQL("SELECT * FROM cp.`employee.json` LIMIT 9");
    assertTrue(ErrorSteps.getFullStackTrace().isEmpty());
  }

}
