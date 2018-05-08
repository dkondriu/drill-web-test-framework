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
package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import pages.PageNavigate;
import pages.PageQuery;
import pages.PageQueryResults;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public abstract class DriverWrapper {
  private enum DRIVER {
    CHROME_WINDOWS,
    CHROME_LINUX,
    FIREFOX_WINDOWS,
    FIREFOX_LINUX
  }
  private static WebDriver driver;
  private static String drillHost;
  private static DRIVER driverType;
  public static PageNavigate pageNavigate = null;
  public static PageQuery pageQuery = null;
  public static PageQueryResults pageQueryResults = null;

  static {
    try (FileInputStream in = new FileInputStream("init.properties")) {
      Properties p = new Properties();
      p.load(in);
      drillHost = p.getProperty("DRILL_HOST");
      driverType = DRIVER.valueOf(p.getProperty("DRIVER_TYPE"));
      System.setProperty("webdriver.chrome.driver", p.getProperty("WEBDRIVERS_PATH"));
    } catch (Exception e) {
      e.printStackTrace();
//      drillHost = "http://192.168.122.199:8047";
//      driverType = DRIVER.CHROME_LINUX;
//      System.setProperty("webdriver.chrome.driver", "webdrivers/chromedriver");
    }
    System.setProperty("webdriver.chrome.driver", "webdrivers/chromedriver");
    //todo: download other webdrivers
    switch (driverType) {
      case CHROME_LINUX:
        driver = new ChromeDriver();
        break;
      default:
        driver = new ChromeDriver();
    }
    driver.get(drillHost);
    maximizeWindow();
  }

  public static WebDriver getDriver() {
    return driver;
  }

  public static void setDriver(WebDriver driver) {
    DriverWrapper.driver = driver;
  }

  public static String getDrillHost() {
    return drillHost;
  }

  public static void setDrillHost(String drillHost) {
    DriverWrapper.drillHost = drillHost;
  }

  public static void openURL(String url) {
    driver.get(drillHost + url);
  }

  public static void maximizeWindow() {
    driver.manage().window().maximize();
  }

  public static void closeBrowser() {
    driver.close();
  }

  public static void waitSeconds(int seconds) {
    try{Thread.sleep(seconds*1000);}catch (Exception ignore){}
    //driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
  }
}
