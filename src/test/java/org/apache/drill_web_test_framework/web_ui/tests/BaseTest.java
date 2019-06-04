package org.apache.drill_web_test_framework.web_ui.tests;

import org.testng.annotations.AfterSuite;
import org.apache.drill_web_test_framework.web_ui.steps.BaseSteps;

public class BaseTest {

  @AfterSuite
  public final void tearDown() {
    BaseSteps.tearDown();
  }
}
