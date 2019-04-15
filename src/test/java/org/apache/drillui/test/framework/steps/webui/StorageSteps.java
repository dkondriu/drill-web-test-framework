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
package org.apache.drillui.test.framework.steps.webui;

import org.apache.drillui.test.framework.pages.StoragePage;

import static org.apache.drillui.test.framework.pages.BasePage.getPage;

public final class StorageSteps extends BaseSteps {

  public static boolean exists(String name) {
    return getPage(StoragePage.class).storagePluginExists(name);
  }

  public static boolean enabled(String name) {
    return getPage(StoragePage.class).storagePluginEnabled(name);
  }

  public static void enable(String name) {
    getPage(StoragePage.class).enableStoragePlugin(name);
  }

  public static void disable(String name) {
    getPage(StoragePage.class).disableStoragePlugin(name);
  }

  public StorageSteps openCreatePluginDialog() {
    getPage(StoragePage.class)
        .openNewStoragePluginDialog();
    return this;
  }

  public static boolean addPluginMode() {
    return getPage(StoragePage.class).formTitlePresented() &&
            getPage(StoragePage.class).pluginNameInputPresented() &&
            getPage(StoragePage.class).closeButtonPresented() &&
            getPage(StoragePage.class).submitButtonPresented();
  }

  public StorageSteps fillNewPluginData(String name, String pluginConfig) {
    getPage(StoragePage.class)
        .setNewStoragePluginName(name)
        .setNewStoragePluginConfig(pluginConfig);
    return this;
  }

  public StorageSteps closeNewPluginForm() {
    getPage(StoragePage.class).closeNewPluginForm();
    return this;
  }

  public StorageSteps submitNewPluginForm() {
    getPage(StoragePage.class).submitNewPluginForm();
    return this;
  }

  public void create(String testPluginName, String testPluginConfig) {
    openCreatePluginDialog();
    fillNewPluginData(testPluginName, testPluginConfig);
    submitNewPluginForm();
  }

  public static void update(String name) {
    getPage(StoragePage.class).updateStoragePlugin(name);
  }
}
