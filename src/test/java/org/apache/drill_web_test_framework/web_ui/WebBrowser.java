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
package org.apache.drill_web_test_framework.web_ui;

import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.apache.drill_web_test_framework.properties.TestProperties;
import org.json.JSONObject;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public abstract class WebBrowser {
  private static String sessionId;
  private static WebDriver driver;
  private static LinkedList<String> parentWindows = new LinkedList<>();

  public static String getSessionId() {
    return sessionId;
  }

  private static Map<String, Class> driverClasses = Stream.of(new Object[][]{
      {"CHROME", ChromeDriver.class},
      {"FIREFOX", FirefoxDriver.class},
      {"IE", InternetExplorerDriver.class},
      {"EDGE", EdgeDriver.class},
  }).collect(Collectors.toMap(data -> (String) data[0], data -> (Class) data[1]));

  private static void init() {
    System.setProperty(
        "webdriver." +
            (PropertiesConst.DRIVER_TYPE.equals("FIREFOX") ?
                "gecko" :
                PropertiesConst.DRIVER_TYPE.toLowerCase())
            + ".driver",
        getWebdriversPath());
    try {
      driver = PropertiesConst.RUN_ON_SELENOID ?
          getRemoteWebDriver(getSelenoidBrowserVersion()) :
          ((WebDriver) driverClasses.get(PropertiesConst.DRIVER_TYPE).newInstance());
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    resetImplicitWait();
    if (PropertiesConst.RUN_ON_SELENOID) {
      driver.manage().window().setSize(new Dimension(1920, 1080));
      openURL("/");
    } else {
      openURL("/");
      maximizeWindow();
    }
  }

  private static WebDriver getRemoteWebDriver(String driverVersion) {
    RemoteWebDriver remoteDriver = null;
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setBrowserName(PropertiesConst.DRIVER_TYPE.toLowerCase());
    capabilities.setVersion(driverVersion);
    capabilities.setCapability("enableVNC", true);
    capabilities.setCapability("enableVideo", false);
    capabilities.setCapability("screenResolution", "1920x1080x24");
    try {
      remoteDriver = new RemoteWebDriver(
          URI.create(PropertiesConst.DRILL_HOST + ":4444/wd/hub").toURL(),
          capabilities
      );
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    sessionId = remoteDriver.getSessionId().toString();
    return remoteDriver;
  }

  private static String getSelenoidBrowserVersion() {
    JSONObject selenoidStatus = new JSONObject(given()
        .get(PropertiesConst.DRILL_HOST + ":4444/status")
        .getBody()
        .print());
    LinkedList<String> versions = new LinkedList<>(
        selenoidStatus.getJSONObject("browsers")
            .getJSONObject(PropertiesConst.DRIVER_TYPE.toLowerCase())
            .keySet());
    if (versions.isEmpty()) {
      throw new RuntimeException("Can't get Selenoid browser version! Status response was - " + selenoidStatus.toString());
    }
    return versions.getLast();
  }

  private static String getWebdriversPath() {
    String path = "webdrivers/" + TestProperties.OS + "_" + PropertiesConst.DRIVER_TYPE;
    if (TestProperties.OS.equals("WINDOWS")) {
      path += ".exe";
    }
    return path;
  }

  public static void setImplicitWait(int seconds) {
    getDriver().manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
  }

  public static void resetImplicitWait() {
    setImplicitWait(PropertiesConst.DEFAULT_TIMEOUT);
  }

  public static WebDriver getDriver() {
    if (driver == null) {
      init();
    }

    return driver;
  }

  public static void openURL(String url) {
    getDriver().get(PropertiesConst.DRILL_HOST + ":" + PropertiesConst.DRILL_PORT + url);
  }

  public static String getURL() {
    URL url;
    try {
      url = new URL(driver.getCurrentUrl());
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    return url.getPath();
  }

  private static void maximizeWindow() {
    driver.manage().window().maximize();
  }

  public static void closeBrowser() {
    if (driver != null) {
      driver.quit();
      driver = null;
    }
  }

  public static void switchToOpenedWindow() {
    parentWindows.push(driver.getWindowHandle());
    for (String window : driver.getWindowHandles()) {
      if (!window.equals(parentWindows.getLast())) {
        driver.switchTo().window(window);
        break;
      }
    }
  }

  public static void closeWindow() {
    driver.close();
    if (parentWindows.size() != 0) {
      driver.switchTo().window(parentWindows.pop());
    }
  }

  public static void waitForWindowOpening(int numberOfOpenWindows) {
    new WebDriverWait(driver, PropertiesConst.DEFAULT_TIMEOUT)
        .until(driver -> driver.getWindowHandles().size() != numberOfOpenWindows);
  }
}
