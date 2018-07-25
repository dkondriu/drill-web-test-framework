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
package initial;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class WebBrowser {
  public enum DRIVER {
    CHROME_WINDOWS, CHROME_LINUX, FIREFOX_WINDOWS, FIREFOX_LINUX
  }

  private static WebDriver driver;

  public static void init() {
    System.setProperty("webdriver.chrome.driver", TestProperties.webdriversPath);
    //todo: download other webdrivers
    //todo: add windows support
    switch (TestProperties.driverType) {
      case CHROME_LINUX:
        driver = new ChromeDriver();
        break;
      default:
        driver = new ChromeDriver();
    }
    driver.get(TestProperties.drillHost);
    maximizeWindow();
  }

  public static WebDriver getDriver() {
    if (driver == null) {
      init();
    }

    return driver;
  }

  public static void openURL(String url) {
    driver.get(TestProperties.drillHost + url);
  }

  public static void maximizeWindow() {
    driver.manage().window().maximize();
  }

  public static void closeBrowser() {
    if(driver != null) {
      driver.quit();
      driver = null;
    }
  }

  public static void waitSeconds(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (Exception ignore) {
    }
    //driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
  }
}
