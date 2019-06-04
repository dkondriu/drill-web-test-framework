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
package org.apache.drill_web_test_framework.web_ui.tests;

import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.apache.drill_web_test_framework.web_ui.steps.AuthSteps;

public class FunctionalTest extends BaseTest {
  private AuthSteps authSteps = new AuthSteps();
  @BeforeSuite
  public final void beforeSuite() {
    if (PropertiesConst.SECURE_DRILL) {
      authSteps.login(PropertiesConst.ADMIN_1_NAME, PropertiesConst.ADMIN_1_PASSWORD);
    }
  }

  @AfterSuite
  public final void afterSuite() {
    if (PropertiesConst.SECURE_DRILL) {
     authSteps.logOut();
    }
  }
}
