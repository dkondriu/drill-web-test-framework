/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class ProfilesPage extends BasePage {
  @FindBy(css = "tbody>tr[role='row']>td>a")
  private List<WebElement> queryResult;

  //CSS
  @FindBy(css = "table[id='profileList_completed']>tbody>tr[role='row']>td:nth-child(2)")
  private List<WebElement> profileNamesList;

  @FindBy(css = "select[name='profileList_completed_length']")
  private WebElement dropDownList;

  @FindBy(css = "select[name='profileList_completed_length']>option[value='-1']")
  private WebElement listItemAll;

  public void selectAllInDropDownList() {
    dropDownList.click();
    listItemAll.click();
  }

  public boolean isQueryProfileINList(String queryProfile) {
    for (WebElement i : queryResult) {
      if (i.getAttribute("href").contains(queryProfile)) return true;
    }
    return false;
  }

  public boolean isUsersNameInProfileList(String userName) {
    for (WebElement i : profileNamesList) {
      if (i.getText().equals(userName)) {
        continue;
      } else {
        return false;
      }
    }
    return true;
  }
}
