package org.apache.drill_web_test_framework.web_ui.tests.query;/*
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

import org.apache.drill_web_test_framework.web_ui.steps.BaseSteps;
import org.apache.drill_web_test_framework.web_ui.steps.NavigationSteps;
import org.apache.drill_web_test_framework.web_ui.steps.QuerySteps;
import org.apache.drill_web_test_framework.web_ui.tests.FunctionalTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class UnionTest extends FunctionalTest{

  private final QuerySteps querySteps = BaseSteps.getSteps(QuerySteps.class);
  private final NavigationSteps navigationSteps = BaseSteps.getSteps(NavigationSteps.class);

  @BeforeMethod
  public void navigateQuery() {
    navigationSteps.navigateQuery();
  }

  @Test (groups = {"functional"})
  public void testQueryUnionWithCastToDifferentDecimals() {

    String query =
      "SELECT cast(1000 as decimal(10,1)) \n"
        + "UNION ALL \n"
        + "SELECT 596.000;";

    String [] expectedResult = {
      "1000.000",
      "596.000"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(),expectedResult);
  }

  @Test (groups = {"functional"})
  public void testQueryUnionWithCastToSameDecimals() {

    String query =
      "SELECT cast(1000 as decimal(10,3)) \n"
        + "UNION ALL \n"
        + "select 596.000;";

    String [] expectedResult = {
      "1000.000",
      "596.000"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(),expectedResult);
  }

  @Test (groups = {"functional"})
  public void testQueryUnionWithCastToOneDecimals() {

    String query =
      "SELECT cast(1000 as decimal(10,1)) \n"
        + "UNION ALL \n"
        + "SELECT cast(596.000 as decimal(10,1))";

    String [] expectedResult = {
      "1000.0",
      "596.0"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(),expectedResult);
  }

  @Test (groups = {"functional"})
  public void testQueryUnionWithCastToThreeDecimals() {

    String query =
      "SELECT cast(1000 as decimal(10,3)) \n"
        + "UNION ALL \n"
        + "SELECT cast(596.000 as decimal(10,3))";

    String [] expectedResult = {
      "1000.000",
      "596.000"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(),expectedResult);
  }

  @Test (groups = {"functional"})
  public void testQueryUnionWithThreeCastsToDecimals() {
    String query =
      "SELECT cast(100 as decimal(10,3))\n"
        + "UNION ALL \n"
        + "SELECT cast(100 as decimal(10,2))\n"
        + "UNION ALL \n"
        + "SELECT cast(100 as decimal(10,1));";

    String[] expectedResult = {
      "100.000",
      "100.000",
      "100.000"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(), expectedResult);
  }

  @Test (groups = {"functional"})
  public void testQueryUnionWithTheeDifferentDecimalsNoCast() {
    String query =
      "SELECT 10.1\n"
        + "UNION ALL \n"
        + "SELECT 20.12\n"
        + "UNION ALL \n"
        + "SELECT 30.123;";

    String[] expectedResult = {
      "10.100",
      "20.120",
      "30.123"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(), expectedResult);
  }

  @Test
  public void testQueryUnionVarcharSubstringAndDate() {
    String query =
      "SELECT SUBSTRING('ABC some text', 1, 3) AS ExtractString\n"
        + "UNION \n"
        + "SELECT date '2017-04-06';"
      ;

    String[] expectedResult = {
      "2017-04-06",
      "ABC"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(), expectedResult);
  }

  @Test
  public void testQueryUnionVarcharAndDate() {
    String query =
      "SELECT 'Awesome text'\n"
        + "UNION \n"
        + "SELECT date '2017-04-06';"
      ;

    String[] expectedResult = {
      "2017-04-06",
      "Awesome text"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(), expectedResult);
  }

  @Test
  public void testQueryUnionVarcharAndDecimal() {
    String query =
      "SELECT 'Awesome text'\n"
        + "UNION \n"
        + "SELECT 10.101;"
      ;

    String[] expectedResult = {
      "10.101",
      "Awesome text"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(), expectedResult);
  }


  @Test
  public void testQueryUnionIntegerAndVarchar() {
    String query =
      "SELECT 'Awesome text'\n"
        + "UNION \n"
        + "SELECT 10.101 \n"
        + "UNION \n"
        + "SELECT 10.101;"
      ;

    String[] expectedResult = {
      "10.101",
      "Awesome text"};

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(), expectedResult);
  }


  @Test
  public void testQueryUnionAllIntegerAndVarchar() {
    String query =
      "SELECT 'Awesome text'\n"
        + "UNION ALL\n"
        + "SELECT 10.101 \n"
        + "UNION ALL\n"
        + "SELECT 10.101;"
      ;

    String[] expectedResult = {
      "Awesome text",
      "10.101",
      "10.101"
    };

    List<String> result = querySteps.getResultsTable(query);
    assertEquals(result.toArray(), expectedResult);
  }





}
