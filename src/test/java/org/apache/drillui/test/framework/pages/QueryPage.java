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

import org.apache.drillui.test.framework.steps.webui.QuerySteps;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.Map;

public class QueryPage extends BasePage {
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

  @FindBy(xpath = "//*[@id=\"queryForm\"]/div[2]/div[1]/label")
  private WebElement queryInputLabel;

  @FindBy(xpath = "//*[@id=\"query-editor-format\"]/textarea")
  private WebElement queryInputField;

  @FindBy(xpath = "//*[@id=\"queryForm\"]/button")
  private WebElement submitButton;

  @FindBy(xpath = "//*[@id=\"queryForm\"]/input[1]")
  private WebElement limitResultsCheckbox;

  @FindBy(id = "queryLimit")
  private WebElement limitResultsInput;

  public WebElement getQueryTypeLabel() {
    return queryTypeLabel;
  }

  public QueryPage limitQueryResults(String rowsInResult) {
    limitResultsCheckbox.click();
    limitResultsInput.clear();
    limitResultsInput.sendKeys(rowsInResult);
    return this;
  }

  public QueryResultsPage submitQuery(String queryText) {
    queryInputField.sendKeys(queryText.replaceAll("\r", "").replaceAll("\n", ""));
    /*((JavascriptExecutor) WebBrowser.getDriver())
        .executeScript("$('input[name=\"query\"]').val(\"" +
            queryText.replaceAll("\r", "").replaceAll("\n", "") +
            "\");");*/
    submitButton.click();
    return getPage(QueryResultsPage.class);
  }

  public QueryPage changeQueryType(QuerySteps.QUERY_TYPE query_type) {
    switch (query_type) {
      case SQL: queryTypeSQLRButton.click();
        break;
      case PHYSICAL: queryTypePHYSICALRButton.click();
        break;
      case LOGICAL: queryTypeLOGICALRButton.click();
        break;
      default: queryTypeSQLRButton.click();
        break;
    }
    return this;
  }

  public QueryPage selectPhysicalQueryType() {
    queryTypePHYSICALRButton.click();
    return this;
  }

  public QueryPage selectLogicalQueryType() {
    queryTypeLOGICALRButton.click();
    return this;
  }

  public void validateQueryPage() {
    Map<String, String> attributes = new HashMap<>();
    String text = "×\nSample SQL query: SELECT * FROM cp.`employee.json` LIMIT 20";

    attributes.put("class", "alert alert-info alert-dismissable");
    attributes.put("style", "font-family: Courier;");
    validateWebElement(sampleQueryElement, attributes, null, text);

    validateWebElement(sampleQueryCloseButton, null, null, null);
    validateWebElement(queryTypeLabel, null, null, null);
    validateWebElement(queryTypeSQLRButton, null, null, null);
    validateWebElement(queryTypePHYSICALRButton, null, null, null);
    validateWebElement(queryTypeLOGICALRButton, null, null, null);
    validateWebElement(queryInputLabel, null, null, null);
    //validateWebElement(queryInputField, null, null, null);
    validateWebElement(submitButton, null, null, null);
  }

  private void validateWebElement(WebElement element, Map<String, String> attributes, Map<String, String> cssValues, String text) {
    if (!element.isDisplayed()) {
      throw new RuntimeException("Element is not displayed!");
    }
    if (!element.isEnabled()) {
      throw new RuntimeException("Element is not enabled!");
    }
    if (attributes == null || attributes.isEmpty()) {
      return;
    }
    for (String attribute : attributes.keySet()) {
      if (!element.getAttribute(attribute).equals(attributes.get(attribute))) {
        throw new RuntimeException("Wrong value of the attribute \"" + attribute + "\"!" + " \nActual \"" + element.getAttribute(attribute) + "\", \nexpected \"" + attributes.get(attribute) + "\"!");
      }
    }
    if (cssValues == null || cssValues.isEmpty()) {
      return;
    }
    for (String cssProperty : cssValues.keySet()) {
      if (!element.getCssValue(cssProperty).equals(cssValues.get(cssProperty))) {
        throw new RuntimeException("Wrong value of the css property \"" + cssProperty + "\"! \nActual \"" + element.getCssValue(cssProperty) + "\", \nexpected \"" + cssValues.get(cssProperty) + "\"!");
      }
    }
    if (text == null) {
      return;
    } else if(!element.getText().equals(text)) {
      throw new RuntimeException("Wrong text of the Web element! \nActual \"" + element.getText() + "\", \nexpected \"" + text + "\"!");
    }
  }

}
