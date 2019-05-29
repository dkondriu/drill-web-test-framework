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
package org.apache.drillui.test.framework.pages;

import com.google.common.base.Stopwatch;
import org.apache.drillui.test.framework.initial.PropertiesConst;
import org.apache.drillui.test.framework.initial.WebBrowser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BasePage {

  private static Map<Class<? extends BasePage>, BasePage> pages;

  BasePage() {
    PageFactory.initElements(getDriver(), this);
  }

  public static <T extends BasePage> T getPage(Class<T> pageClass) {

    if (pages == null) {
      pages = new HashMap<>();
    }

    if (!pages.containsKey(pageClass)) {
      try {
        pages.put(pageClass, pageClass.newInstance());
      } catch (InstantiationException | IllegalAccessException e) {
        WebBrowser.closeBrowser();
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }

    return pageClass.cast(pages.get(pageClass));
  }

  public static void clearPages() {
    pages.clear();
  }

  protected static WebDriver getDriver() {
    return WebBrowser.getDriver();
  }

  protected static void sendText(WebElement element, String text) {
    Toolkit.getDefaultToolkit().getSystemClipboard()
        .setContents(new StringSelection(text), null);
    element.sendKeys(Keys.CONTROL, "a");
    element.sendKeys(Keys.CONTROL, "v");
  }

  protected <V> void waitForCondition(Function<WebDriver, V> condition) {
    waitForCondition(condition, PropertiesConst.DEFAULT_TIMEOUT);
  }

  protected <V> void waitForCondition(Function<WebDriver, V> condition, int timeOut) {
      new WebDriverWait(getDriver(), timeOut)
          .until(condition);
  }

  protected void waitForCondition(Function<WebDriver, Boolean> condition, Runnable workaroundAction) {
    int timeStep = 1;
    Stopwatch stopwatch = Stopwatch.createStarted();
    boolean success = false;
    while (!condition.apply(getDriver()) &&
        stopwatch.elapsed(TimeUnit.SECONDS) < PropertiesConst.DEFAULT_TIMEOUT - 1) {
      try {
        waitForCondition(condition, timeStep);
        success = true;
      } catch (Exception e) {
        workaroundAction.run();
      }
    }
    if (!success) {
      waitForCondition(condition, timeStep);
    }
  }

  protected boolean isElementStable(WebElement element) {
    Point position = element.getLocation();
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return position.equals(element.getLocation());
  }

  protected List<List<String>> getTable(WebElement table) {
    String tableSource = table.getAttribute("outerHTML");
    List<List<String>> result = getTableHeader(tableSource);
    result.addAll(getTableContent(tableSource));
    return result;
  }

  private List<List<String>> getTableHeader(String table) {
    return Jsoup.parse(table)
        .select("thead")
        .select("tr")
        .stream()
        .map(header -> header.select("th")
            .stream()
            .map(Element::text)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }

  private List<List<String>> getTableContent(String table) {
    return Jsoup.parse(table)
        .outputSettings(new Document.OutputSettings()
            .prettyPrint(false))
        .select("tbody")
        .select("tr")
        .stream()
        .map(row -> row.select("td")
            .stream()
            .map(Element::html)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }
}
