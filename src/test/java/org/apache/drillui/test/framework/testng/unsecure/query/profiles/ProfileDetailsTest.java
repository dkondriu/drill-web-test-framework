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

package org.apache.drillui.test.framework.testng.unsecure.query.profiles;

import org.apache.drillui.test.framework.steps.restapi.RestBaseSteps;
import org.apache.drillui.test.framework.steps.webui.QueryProfileDetailsSteps;
import org.apache.drillui.test.framework.steps.webui.QueryResultsSteps;
import org.apache.drillui.test.framework.steps.webui.QuerySteps;
import org.apache.drillui.test.framework.testng.unsecure.BaseUnsecureTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ProfileDetailsTest extends BaseUnsecureTest {

  private String testQuery = "select * from cp.`tpch/nation.parquet` limit 5";

  @BeforeClass
  public void setupProfileDetailsTest() {
    RestBaseSteps.setupStoragePlugins();
    QuerySteps.runQuery(testQuery);
    QueryProfileDetailsSteps.openProfile(QueryResultsSteps.getQueryProfile());
  }

  @Test
  public void ValidateProfileDetailsPage() {
    QueryProfileDetailsSteps.validatePage();
  }

  @Test
  public void QueryTabTest() {
    QueryProfileDetailsSteps.navigateQueryTab();
    assertEquals(QueryProfileDetailsSteps.activeTab(), "Query");
    assertEquals(QueryProfileDetailsSteps.getQueryText(), testQuery);
    QueryProfileDetailsSteps.setQueryText("Edited query");
  }

}
