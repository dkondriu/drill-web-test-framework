package org.apache.drillui.test.framework.testng;

import org.testng.annotations.AfterSuite;
import org.apache.drillui.test.framework.steps.BaseSteps;

public class BaseTest {

  @AfterSuite
  public void tearDown() {
    BaseSteps.tearDown();
  }
}
