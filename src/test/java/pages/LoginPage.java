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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {
  @FindBy(xpath = "/html/body/div[2]/div[2]/form/fieldset/div/h4")
  private WebElement loginTitle;
  @FindBy(xpath = "//*[@name=\"j_username\"]")
  private WebElement userName;
  @FindBy(xpath = "//*[@name=\"j_password\"]")
  private WebElement userPassword;
  @FindBy(xpath = "/html/body/div[2]/div[2]/form/fieldset/div/p[3]/button")
  private WebElement submitLogin;

  public String getLoginTitle() {
    return loginTitle.getText();
  }

  public String getUserName() {
    return userName.getAttribute("value");
  }

  public void setUserName(String userName) {
    this.userName.sendKeys(userName);
  }

  public String getUserPassword() {
    return userPassword.getAttribute("value");
  }

  public void setUserPassword(String userPassword) {
    this.userPassword.sendKeys(userPassword);
  }

  public NavigationPage submit() {
    submitLogin.click();
    return getPage(NavigationPage.class);
  }
}
