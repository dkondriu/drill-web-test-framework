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

import org.testng.annotations.*;
import steps.QuerySteps;
import testng.unsecure.BaseUnsecureTest;

import static org.testng.Assert.assertTrue;

public class DateOutputTest extends BaseUnsecureTest {
  /*@BeforeSuite
  public void beforeSuite() {

  }

  @BeforeMethod
  public void beforeMethod() {

  }*/

  @Test(groups = { "functional" })
  public void selectDateQuery() {
    String query = "select date '2017-04-06';";
    String result = QuerySteps.runSimpleQuery(query);
    String expected = "2017-04-06";
    assert expected.equals(result) : "Expected date " + expected + ", for " + result;
  }

  @Test(groups = { "functional" })
  public void selectDateQuery2() {
    String query = "SELECT * FROM cp.`employee.json`;";
    String result = QuerySteps.runSimpleQuery(query);
    String expected = "1";
    assert expected.equals(result) : "Expected date " + expected + ", for " + result;
  }

  @Test(groups = { "functional" })
  public void selectTimestampZeroQuery() {
    String query = "select timestamp '2017-04-06 11:22:33';";
    String result = QuerySteps.runSimpleQuery(query);
    String expected = "2017-04-06 11:22:33.0";
    assert expected.equals(result) : "Expected timestamp " + expected + ", for " + result;
  }

  @Test(groups = { "functional" })
  public void selectTimestampOneQuery() {
    String query = "select timestamp '2017-04-06 11:22:33.1';";
    String result = QuerySteps.runSimpleQuery(query);
    String expected = "2017-04-06 11:22:33.1";
    assert expected.equals(result) : "Expected timestamp " + expected + ", for " + result;
  }

  @Test(groups = { "functional" })
  public void selectTimestampTwoQuery() {
    String query = "select timestamp '2017-04-06 11:22:33.12';";
    String result = QuerySteps.runSimpleQuery(query);
    String expected = "2017-04-06 11:22:33.12";
    assert expected.equals(result) : "Expected timestamp " + expected + ", for " + result;
  }

  @Test(groups = { "functional" })
  public void selectTimestampThreeQuery() {
    String query = "select timestamp '2017-04-06 11:22:33.123';";
    String result = QuerySteps.runSimpleQuery(query);
    String expected = "2017-04-06 11:22:33.123";
    assert expected.equals(result) : "Expected timestamp " + expected + ", for " + result;
  }

  @Test(groups = { "functional" })
  public void selectTimeQuery() {
    String query = "select time '11:22:33';";
    String result = QuerySteps.runSimpleQuery(query);
    String expected = "11:22:33";
    assert expected.equals(result) : "Expected time " + expected + ", for " + result;
  }

  /*@AfterSuite
  public void afterSuite() {
    DriverWrapper.closeBrowser();
  }*/

}
