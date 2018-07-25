package testng;

import initial.WebBrowser;
import org.testng.annotations.AfterSuite;
import pages.BasePage;
import steps.BaseSteps;

public class BaseTest {

  @AfterSuite
  public void tearDown() {
    BaseSteps.tearDown();
  }
}
