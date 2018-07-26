package testng;

import org.testng.annotations.AfterSuite;
import steps.BaseSteps;

public class BaseTest {

  @AfterSuite
  public void tearDown() {
    BaseSteps.tearDown();
  }
}
