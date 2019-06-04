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
package org.apache.drill_web_test_framework.web_ui.steps;

import org.apache.drill_web_test_framework.web_ui.pages.StoragePage;

import static org.apache.drill_web_test_framework.web_ui.pages.BasePage.getPage;
import static org.apache.drill_web_test_framework.web_ui.steps.ConfirmDialogSteps.getConfirmDialog;

public final class StorageSteps extends BaseSteps {

  public static boolean exists(String name) {
    return getStoragePage().storagePluginExists(name);
  }

  public static boolean enabled(String name) {
    return getStoragePage().storagePluginEnabled(name);
  }

  public static void enable(String name) {
    getStoragePage().enableStoragePlugin(name);
  }

  public static void disable(String name) {
    getStoragePage().disableStoragePlugin(name);
    getConfirmDialog().confirmAction();
    getStoragePage().waitStoragePluginToBeDisabled(name);
  }

  public StorageSteps openCreatePluginDialog() {
    getStoragePage()
        .openNewStoragePluginDialog();
    return this;
  }

  public static boolean addPluginMode() {
    return getStoragePage().formTitlePresented() &&
            getStoragePage().pluginNameInputPresented() &&
            getStoragePage().closeButtonPresented() &&
            getStoragePage().submitButtonPresented();
  }

  public StorageSteps fillNewPluginData(String name, String pluginConfig) {
    getStoragePage()
        .setNewStoragePluginName(name)
        .setNewStoragePluginConfig(pluginConfig);
    return this;
  }

  public StorageSteps closeNewPluginForm() {
    getStoragePage().closeNewPluginForm();
    return this;
  }

  public StorageSteps submitNewPluginForm() {
    getStoragePage().submitNewPluginForm();
    return this;
  }

  public void create(String testPluginName, String testPluginConfig) {
    openCreatePluginDialog();
    fillNewPluginData(testPluginName, testPluginConfig);
    submitNewPluginForm();
  }

  public static void update(String name) {
    getStoragePage().updateStoragePlugin(name);
  }

  private static StoragePage getStoragePage() {
    return getPage(StoragePage.class);
  }
}
