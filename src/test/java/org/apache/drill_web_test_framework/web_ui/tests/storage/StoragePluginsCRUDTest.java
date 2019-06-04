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
package org.apache.drill_web_test_framework.web_ui.tests.storage;

import org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps;
import org.apache.drill_web_test_framework.web_ui.steps.BaseSteps;
import org.apache.drill_web_test_framework.web_ui.steps.EditStoragePluginSteps;
import org.apache.drill_web_test_framework.web_ui.steps.NavigationSteps;
import org.apache.drill_web_test_framework.web_ui.steps.StorageSteps;
import org.apache.drill_web_test_framework.web_ui.tests.FunctionalTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.apache.drill_web_test_framework.web_ui.steps.ConfirmDialogSteps.getConfirmDialog;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class StoragePluginsCRUDTest extends FunctionalTest {
  private final StorageSteps storageSteps = BaseSteps.getSteps(StorageSteps.class);
  private final NavigationSteps navigationSteps = BaseSteps.getSteps(NavigationSteps.class);

  private String testPluginConfig =
      "{\n" +
      "  \"type\": \"kafka\",\n" +
      "  \"kafkaConsumerProps\": {\n" +
      "    \"bootstrap.servers\": \"localhost:9092\",\n" +
      "    \"group.id\": \"drill-consumer\"\n" +
      "  },\n" +
      "  \"enabled\": false\n" +
      "}";

  private String testPluginName = "kafka_2";

  @BeforeClass
  public void setupStoragePlugins() {
    RestBaseSteps.setupStoragePlugins();
  }

  @BeforeMethod
  public void beforeMethod() {
    navigationSteps.navigateStorage();
    if (StorageSteps.exists(testPluginName)) {
      StorageSteps.update(testPluginName);
      EditStoragePluginSteps.delete();
      getConfirmDialog().confirmAction();
      BaseSteps.waitForURL("/storage");
    }
  }

  @Test(groups = {"functional"})
  public void addPluginCancel() {
    storageSteps.openCreatePluginDialog();
    assertTrue(StorageSteps.addPluginMode());
    storageSteps.fillNewPluginData(testPluginName, testPluginConfig)
        .closeNewPluginForm();
    assertEquals(BaseSteps.getURL(), "/storage");
    assertFalse(StorageSteps.exists(testPluginName));
  }

  @Test(groups = {"functional"})
  public void addPluginCreate() {
    storageSteps.openCreatePluginDialog();
    assertTrue(StorageSteps.addPluginMode());
    storageSteps.fillNewPluginData(testPluginName, testPluginConfig)
            .submitNewPluginForm();
    assertEquals(BaseSteps.getURL(), "/storage");
    assertTrue(StorageSteps.exists(testPluginName));
    assertFalse(StorageSteps.enabled(testPluginName));
  }

  @Test(groups = {"functional"})
  public void addPluginExisting() {
    storageSteps.openCreatePluginDialog()
        .fillNewPluginData(testPluginName, testPluginConfig.replace("\"enabled\": false", "\"enabled\": true"))
        .submitNewPluginForm();
    assertTrue(StorageSteps.enabled(testPluginName));
  }

  @Test(groups = {"functional"})
  public void updatePluginUnableToParse() {
    storageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    String modifiedConfig = EditStoragePluginSteps.getPluginConfig()
        .toUpperCase();
    EditStoragePluginSteps.setPluginConfig(modifiedConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "Please retry: Error (unable to parse JSON)");
    EditStoragePluginSteps.back();
    StorageSteps.update(testPluginName);
    assertEquals(EditStoragePluginSteps.getPluginConfig(), testPluginConfig);
  }

  @Test(groups = {"functional"})
  public void updatePluginUnableInvalidMapping() {
    storageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    String modifiedConfig = EditStoragePluginSteps.getPluginConfig()
        .replace("kafka", "KAFKA");
    EditStoragePluginSteps.setPluginConfig(modifiedConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "Please retry: Error (invalid JSON mapping)");
    EditStoragePluginSteps.back();
    StorageSteps.update(testPluginName);
    assertEquals(EditStoragePluginSteps.getPluginConfig(), testPluginConfig);
  }

  @Test(groups = {"functional"})
  public void updatePluginSuccess() {
    storageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    String modifiedConfig = EditStoragePluginSteps.getPluginConfig()
        .replace("9092", "1111");
    EditStoragePluginSteps.setPluginConfig(modifiedConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "Success");
    BaseSteps.waitForURL("/storage");
    StorageSteps.update(testPluginName);
    assertEquals(EditStoragePluginSteps.getPluginConfig(), modifiedConfig);
  }

  @Test(groups = {"functional"})
  public void updatePluginEnableDisable() {
    storageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    String modifiedConfig = EditStoragePluginSteps.getPluginConfig()
        .replace("\"enabled\": false", "\"enabled\": true");
    EditStoragePluginSteps.setPluginConfig(modifiedConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "Success");
    BaseSteps.waitForURL("/storage");
    assertTrue(StorageSteps.enabled(testPluginName));
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.setPluginConfig(testPluginConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "Success");
    BaseSteps.waitForURL("/storage");
    assertFalse(StorageSteps.enabled(testPluginName));
  }

  @Test(groups = {"functional"})
  public void enableDisablePlugin() {
    storageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.enable();
    assertEquals(EditStoragePluginSteps.getMessage(), "Success");
    assertTrue(EditStoragePluginSteps.waitForEnabled());
    EditStoragePluginSteps.back();
    assertTrue(StorageSteps.enabled(testPluginName));
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.disable();
    getConfirmDialog().confirmAction();
    assertEquals(EditStoragePluginSteps.getMessage(), "Success");
    assertTrue(EditStoragePluginSteps.waitForDisabled());
    EditStoragePluginSteps.back();
    assertFalse(StorageSteps.enabled(testPluginName));
  }

  @Test(groups = {"functional"})
  public void deletePlugin() {
    storageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.delete();
    getConfirmDialog().cancelAction();
    assertEquals(BaseSteps.getURL(), "/storage/" + testPluginName);
    EditStoragePluginSteps.back();
    assertTrue(StorageSteps.exists(testPluginName));
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.delete();
    getConfirmDialog().confirmAction();
    BaseSteps.waitForURL("/storage");
    assertFalse(StorageSteps.exists(testPluginName));
  }
}
