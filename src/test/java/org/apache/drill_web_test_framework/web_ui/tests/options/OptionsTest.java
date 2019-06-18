/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.drill_web_test_framework.web_ui.tests.options;

import org.apache.drill_web_test_framework.web_ui.steps.BaseSteps;
import org.apache.drill_web_test_framework.web_ui.steps.NavigationSteps;
import org.apache.drill_web_test_framework.web_ui.steps.OptionsSteps;
import org.apache.drill_web_test_framework.web_ui.tests.FunctionalTest;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OptionsTest extends FunctionalTest {
  private final OptionsSteps optionsSteps = BaseSteps.getSteps(OptionsSteps.class);
  private final NavigationSteps navigationSteps = BaseSteps.getSteps(NavigationSteps.class);
  private final String numericOptionName = "drill.exec.memory.operator.output_batch_size";
  private final String stringOptionName = "drill.exec.storage.file.partition.column.label";
  private final String booleanOptionName = "drill.exec.functions.cast_empty_string_to_null";

  @Test
  public void sortingByOption() {
    List<List<String>> table = navigationSteps.navigateOptions()
        .getOptionsTable();
    optionsSteps.sortByOption();
    List<List<String>> sortedTable = optionsSteps.getOptionsTable();
    table.remove(0);
    sortedTable.remove(0);
    Collections.reverse(table);
    assertEquals(sortedTable, table);
    optionsSteps.sortByOption();
  }

  @Test
  public void sortingByPlanner() {
    String planner = "planner";
    List<List<String>> table = navigationSteps.navigateOptions()
        .selectQuickFilter(planner)
        .getOptionsTable();
    assertTrue(optionsSteps.isSearchFieldFilled(planner));
    table.stream().skip(1).forEach(x -> {
      assertTrue(x.get(0).contains(planner) || x.get(2).contains(planner));
    });
  }

  @Test
  public void sortingByStore() {
    String store = "store";
    List<List<String>> table = navigationSteps.navigateOptions()
        .selectQuickFilter(store)
        .getOptionsTable();
    assertTrue(optionsSteps.isSearchFieldFilled(store));
    table.stream().skip(1).forEach(x -> {
      assertTrue(x.get(0).contains(store) || x.get(2).contains(store));
    });
  }

  @Test
  public void sortingByParquet() {
    String parquet = "parquet";
    List<List<String>> table = navigationSteps.navigateOptions()
        .selectQuickFilter(parquet)
        .getOptionsTable();
    assertTrue(optionsSteps.isSearchFieldFilled(parquet));
    table.stream().skip(1).forEach(x -> {
      assertTrue(x.get(0).contains(parquet) || x.get(2).contains(parquet) || x.get(2).contains(parquet.replace("p", "P")));
    });
  }

  @Test
  public void sortingByHashagg() {
    String hashagg = "hashagg";
    List<List<String>> table = navigationSteps.navigateOptions()
        .selectQuickFilter(hashagg)
        .getOptionsTable();
    assertTrue(optionsSteps.isSearchFieldFilled(hashagg));
    table.stream().skip(1).forEach(x -> {
      assertTrue(x.get(0).contains(hashagg) || x.get(2).contains(hashagg));
    });
  }

  @Test
  public void sortingByHashjoin() {
    String hashjoin = "hashjoin";
    List<List<String>> table = navigationSteps.navigateOptions()
        .selectQuickFilter(hashjoin)
        .getOptionsTable();
    assertTrue(optionsSteps.isSearchFieldFilled(hashjoin));
    table.stream().skip(1).forEach(x -> {
      assertTrue(x.get(0).contains(hashjoin) || x.get(2).contains(hashjoin));
    });
  }

  @Test
  public void searchFieldTest() {
    String searchParameter = "drill.exec";
    assertTrue(navigationSteps.navigateOptions()
        .searchFieldSendKeys(searchParameter)
        .isSearchFieldFilled(searchParameter));
    List<List<String>> table = optionsSteps.getOptionsTable();
    table.stream().skip(1).forEach(x -> {
      assertTrue(x.get(0).contains(searchParameter) || x.get(2).contains(searchParameter));
    });
    assertTrue(optionsSteps.clearSearchField()
        .isSearchFieldFilled(""));
  }

  @Test
  public void updateBooleanField() {
    assertTrue(navigationSteps.navigateOptions()
        .updateBooleanTrueOrFalse(booleanOptionName, "true")
        .isBooleanValueAppropriate(booleanOptionName, "true"));
    assertTrue(optionsSteps.setOptionToDefault(booleanOptionName)
        .isBooleanValueAppropriate(booleanOptionName, "false"));
  }

  @Test
  public void updateNumericField() {
    String updatedValue = "16777217";
    String defaultValue = "16777216";
    assertTrue(navigationSteps.navigateOptions()
        .updateFieldOption(numericOptionName, updatedValue)
        .isValueAppropriate(numericOptionName, updatedValue));
    assertTrue(
        optionsSteps.setOptionToDefault(numericOptionName)
            .isValueAppropriate(numericOptionName, defaultValue));
  }

  @Test
  public void updateStringField() {
    String updatedValueOfString = "test";
    String defaultValueOfString = "dir";
    assertTrue(navigationSteps.navigateOptions()
        .updateFieldOption(stringOptionName, updatedValueOfString)
        .isValueAppropriate(stringOptionName, updatedValueOfString));
    assertTrue(optionsSteps.setOptionToDefault(stringOptionName)
        .isValueAppropriate(stringOptionName, defaultValueOfString));
  }
}
