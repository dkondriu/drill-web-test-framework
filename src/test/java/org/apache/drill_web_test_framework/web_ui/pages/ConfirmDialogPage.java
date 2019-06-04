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
package org.apache.drill_web_test_framework.web_ui.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ConfirmDialogPage extends BasePage {
  @FindBy(id = "confirmationModal")
  private WebElement parrentDialogForm;

  @FindBy(id = "modalBody")
  private WebElement dialogMessage;

  @FindBy(xpath = "//*[@id='confirmationModal']//*/button[contains(@class, 'closeX')]")
  private WebElement closeDialogButton;

  @FindBy(id = "confirmationOk")
  private WebElement confirmDialogButton;

  @FindBy(id = "confirmationCancel")
  private WebElement cancelDialogButton;

  public String getDialogMessage() {
    waitForCondition(driver -> dialogMessage.isDisplayed());
    return dialogMessage.getText();
  }

  public void closeDialog() {
    clickOnElement(closeDialogButton);
  }

  private void clickOnElement(WebElement element) {
    waitForCondition(driver -> element.isDisplayed());
    waitForCondition(ExpectedConditions.elementToBeClickable(element));
    element.click();
    waitForCondition(ExpectedConditions.invisibilityOf(element));
  }

  public void clickOutsideDialog() {
    clickOnElement(parrentDialogForm);
  }

  public void confirmAction() {
    clickOnElement(confirmDialogButton);
  }

  public void cancelAction() {
    clickOnElement(cancelDialogButton);
  }
}
