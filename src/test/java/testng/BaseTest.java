package testng;

import initial.WebBrowser;
import org.testng.annotations.AfterSuite;

public class BaseTest {

  @AfterSuite
  public void tearDown() {
    WebBrowser.closeBrowser();
  }
}
