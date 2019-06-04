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

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.drill_web_test_framework.web_ui.pages.BasePage;
import org.apache.drill_web_test_framework.web_ui.pages.QueryExceptionPage;
import org.apache.drill_web_test_framework.web_ui.steps.BaseSteps;
import org.apache.drill_web_test_framework.web_ui.steps.ErrorSteps;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestEventsListener implements ITestListener {
  private static final String LOG_LINES_SEPARATOR = "-------------------------------------";

  @Override
  public void onTestStart(ITestResult iTestResult) {

  }

  @Override
  public void onTestSuccess(ITestResult iTestResult) {

  }

  @Override
  public void onTestFailure(ITestResult iTestResult) {
    String error = getError();
    if (error.isEmpty()) {
      return;
    }
    Throwable throwable = iTestResult.getThrowable();
    String originalMessage = throwable.getMessage();
    String newMessage = "\n" + LOG_LINES_SEPARATOR + "\nDrill error:\n\n" + error + "\n\n" + LOG_LINES_SEPARATOR +
        "\nFramework error:\n\n" + originalMessage;
    try {
      FieldUtils.writeField(throwable, "detailMessage", newMessage, true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onTestSkipped(ITestResult iTestResult) {

  }

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

  }

  @Override
  public void onStart(ITestContext iTestContext) {

  }

  @Override
  public void onFinish(ITestContext iTestContext) {

  }

  public static String getError() {
    String error = ErrorSteps.getFullStackTrace();
    if (!error.isEmpty()) {
      BaseSteps.openUrl("/");
      return error;
    } else {
      return BasePage.getPage(QueryExceptionPage.class).getFullStackTrace();
    }
  }
}
