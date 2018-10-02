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
package org.apache.drillui.test.framework.testng.unsecure.storage;

import org.apache.drillui.test.framework.steps.webui.AlertSteps;
import org.apache.drillui.test.framework.steps.webui.BaseSteps;
import org.apache.drillui.test.framework.steps.webui.EditStoragePluginSteps;
import org.apache.drillui.test.framework.steps.webui.NavigateSteps;
import org.apache.drillui.test.framework.steps.webui.StorageSteps;
import org.apache.drillui.test.framework.testng.unsecure.BaseUnsecureTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class StoragePluginsCRUDTest extends BaseUnsecureTest {

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
    org.apache.drillui.test.framework.steps.restapi.BaseSteps.setupStoragePlugins();
  }

  @BeforeMethod
  public void beforeMethod() {
    NavigateSteps.navigateStorage();
    if (StorageSteps.exists(testPluginName)) {
      StorageSteps.update(testPluginName);
      EditStoragePluginSteps.delete();
      AlertSteps.acceptAlert();
    }
  }

  @Test(groups = {"functional"})
  public void addPluginCancel() {
    StorageSteps.create(testPluginName);
    assertTrue(EditStoragePluginSteps.addPluginMode());
    assertEquals(EditStoragePluginSteps.getPluginConfig(), "null");
    EditStoragePluginSteps.setPluginConfig(testPluginConfig);
    EditStoragePluginSteps.back();
    assertEquals(BaseSteps.getURL(), "/storage");
    assertFalse(StorageSteps.exists(testPluginName));
  }

  @Test(groups = {"functional"})
  public void addPluginCreate() {
    StorageSteps.create(testPluginName);
    EditStoragePluginSteps.setPluginConfig(testPluginConfig);
    EditStoragePluginSteps.create();
    assertTrue(EditStoragePluginSteps.editPluginMode());
    EditStoragePluginSteps.back();
    assertEquals(BaseSteps.getURL(), "/storage");
    assertTrue(StorageSteps.exists(testPluginName));
  }

  @Test(groups = {"functional"})
  public void addPluginExisting() {
    StorageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.create(testPluginName);
    assertTrue(EditStoragePluginSteps.editPluginMode());
  }

  @Test(groups = {"functional"})
  public void updatePluginUnableToParse() {
    StorageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    String modifiedConfig = EditStoragePluginSteps.getPluginConfig()
        .toUpperCase();
    EditStoragePluginSteps.setPluginConfig(modifiedConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "Please retry: error (unable to parse JSON)");
    EditStoragePluginSteps.back();
    StorageSteps.update(testPluginName);
    assertEquals(EditStoragePluginSteps.getPluginConfig(), testPluginConfig);
  }

  @Test(groups = {"functional"})
  public void updatePluginUnableInvalidMapping() {
    StorageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    String modifiedConfig = EditStoragePluginSteps.getPluginConfig()
        .replace("kafka", "KAFKA");
    EditStoragePluginSteps.setPluginConfig(modifiedConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "Please retry: error (invalid JSON mapping)");
    EditStoragePluginSteps.back();
    StorageSteps.update(testPluginName);
    assertEquals(EditStoragePluginSteps.getPluginConfig(), testPluginConfig);
  }

  @Test(groups = {"functional"})
  public void updatePluginSuccess() {
    StorageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    String modifiedConfig = EditStoragePluginSteps.getPluginConfig()
        .replace("9092", "1111");
    EditStoragePluginSteps.setPluginConfig(modifiedConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "success");
    EditStoragePluginSteps.back();
    StorageSteps.update(testPluginName);
    assertEquals(EditStoragePluginSteps.getPluginConfig(), modifiedConfig);
  }

  @Test(groups = {"functional"})
  public void updatePluginEnableDisable() {
    StorageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    String modifiedConfig = EditStoragePluginSteps.getPluginConfig()
        .replace("\"enabled\": false", "\"enabled\": true");
    EditStoragePluginSteps.setPluginConfig(modifiedConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "success");
    assertTrue(EditStoragePluginSteps.waitForEnabled());
    EditStoragePluginSteps.back();
    assertTrue(StorageSteps.enabled(testPluginName));
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.setPluginConfig(testPluginConfig);
    EditStoragePluginSteps.update();
    assertEquals(EditStoragePluginSteps.getMessage(), "success");
    assertTrue(EditStoragePluginSteps.waitForDisabled());
    EditStoragePluginSteps.back();
    assertFalse(StorageSteps.enabled(testPluginName));
  }

  @Test(groups = {"functional"})
  public void enableDisablePlugin() {
    StorageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.enable();
    assertEquals(EditStoragePluginSteps.getMessage(), "success");
    assertTrue(EditStoragePluginSteps.waitForEnabled());
    EditStoragePluginSteps.back();
    assertTrue(StorageSteps.enabled(testPluginName));
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.disable();
    assertEquals(EditStoragePluginSteps.getMessage(), "success");
    assertTrue(EditStoragePluginSteps.waitForDisabled());
    EditStoragePluginSteps.back();
    assertFalse(StorageSteps.enabled(testPluginName));
  }

  @Test(groups = {"functional"})
  public void deletePlugin() {
    StorageSteps.create(testPluginName, testPluginConfig);
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.delete();
    AlertSteps.declineAlert();
    assertEquals(BaseSteps.getURL(), "/storage/" + testPluginName);
    EditStoragePluginSteps.back();
    assertTrue(StorageSteps.exists(testPluginName));
    StorageSteps.update(testPluginName);
    EditStoragePluginSteps.delete();
    AlertSteps.acceptAlert();
    BaseSteps.waitForURL("/storage");
    assertFalse(StorageSteps.exists(testPluginName));
  }
}
