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

import driver.DriverWrapper;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageQuery extends PageBase {

  @FindBy(xpath = "//*[@id=\"message\"]")
  private WebElement sampleQueryElement;
  @FindBy(xpath = "//*[@id=\"message\"]/button")
  private WebElement sampleQueryCloseButton;
  @FindBy(xpath = "//*[@id=\"queryForm\"]/div[1]/label")
  private WebElement queryTypeLabel;
  @FindBy(xpath = "//*[@id=\"sql\"]")
  private WebElement queryTypeSQLRButton;
  @FindBy(xpath = "//*[@id=\"physical\"]")
  private WebElement queryTypePHYSICALRButton;
  @FindBy(xpath = "//*[@id=\"logical\"]")
  private WebElement queryTypeLOGICALRButton;
  @FindBy(xpath = "//*[@id=\"queryForm\"]/div[2]/label")
  private WebElement queryInputLabel;
  @FindBy(xpath = "//*[@id=\"query-editor-format\"]/textarea")
  private WebElement queryInputField;
  @FindBy(xpath = "//*[@id=\"queryForm\"]/button")
  private WebElement submitButton;

  public PageQuery() {
    DriverWrapper.pageQuery = this;
  }

  public WebElement getSampleQueryElement() {
    return sampleQueryElement;
  }

  public void setSampleQueryElement(WebElement sampleQueryElement) {
    this.sampleQueryElement = sampleQueryElement;
  }

  public WebElement getSampleQueryCloseButton() {
    return sampleQueryCloseButton;
  }

  public void setSampleQueryCloseButton(WebElement sampleQueryCloseButton) {
    this.sampleQueryCloseButton = sampleQueryCloseButton;
  }

  public WebElement getQueryTypeLabel() {
    return queryTypeLabel;
  }

  public void setQueryTypeLabel(WebElement queryTypeLabel) {
    this.queryTypeLabel = queryTypeLabel;
  }

  public WebElement getQueryTypeSQLRButton() {
    return queryTypeSQLRButton;
  }

  public void setQueryTypeSQLRButton(WebElement queryTypeSQLRButton) {
    this.queryTypeSQLRButton = queryTypeSQLRButton;
  }

  public WebElement getQueryTypePHYSICALRButton() {
    return queryTypePHYSICALRButton;
  }

  public void setQueryTypePHYSICALRButton(WebElement queryTypePHYSICALRButton) {
    this.queryTypePHYSICALRButton = queryTypePHYSICALRButton;
  }

  public WebElement getQueryTypeLOGICALRButton() {
    return queryTypeLOGICALRButton;
  }

  public void setQueryTypeLOGICALRButton(WebElement queryTypeLOGICALRButton) {
    this.queryTypeLOGICALRButton = queryTypeLOGICALRButton;
  }

  public WebElement getQueryInputLabel() {
    return queryInputLabel;
  }

  public void setQueryInputLabel(WebElement queryInputLabel) {
    this.queryInputLabel = queryInputLabel;
  }

  public WebElement getQueryInputField() {
    return queryInputField;
  }

  public void setQueryInputField(WebElement queryInputField) {
    this.queryInputField = queryInputField;
  }

  public WebElement getSubmitButton() {
    return submitButton;
  }

  public void setSubmitButton(WebElement submitButton) {
    this.submitButton = submitButton;
  }

  public void submitQuery(String queryText) {
    getQueryInputField().sendKeys(queryText);
    getSubmitButton().click();
    new PageQueryResults();
  }

}
