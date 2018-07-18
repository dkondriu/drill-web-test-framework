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
package pages;

import initial.WebBrowser;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class NavigatePage extends BasePage {
  public static NavigatePage navigatePage = null;
  @FindBy(partialLinkText = "Apache Drill")
  private WebElement home;
  @FindBy(partialLinkText = "Query")
  private WebElement query;
  @FindBy(partialLinkText = "Profiles")
  private WebElement profiles;
  @FindBy(partialLinkText = "Storage")
  private WebElement storage;
  @FindBy(partialLinkText = "Metrics")
  private WebElement metrics;
  @FindBy(partialLinkText = "Threads")
  private WebElement threads;
  @FindBy(partialLinkText = "Logs")
  private WebElement logs;
  @FindBy(partialLinkText = "Options")
  private WebElement options;
  @FindBy(partialLinkText = "Documentation")
  private WebElement documentation;

  public NavigatePage() {
    navigatePage = this;
  }

  public void openQueryPage() {
    query.click();
  }

  public void pagesDemo() {
    home.click();
    WebBrowser.waitSeconds(2);
    query.click();
    WebBrowser.waitSeconds(2);
    profiles.click();
    WebBrowser.waitSeconds(2);
    storage.click();
    WebBrowser.waitSeconds(2);
    metrics.click();
    WebBrowser.waitSeconds(2);
    threads.click();
    WebBrowser.waitSeconds(2);
    logs.click();
    WebBrowser.waitSeconds(2);
    options.click();
    WebBrowser.waitSeconds(2);
  }
}
