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

import org.apache.drillui.test.framework.steps.restapi.RestBaseSteps;
import org.apache.drillui.test.framework.steps.webui.NavigationSteps;
import org.apache.drillui.test.framework.steps.webui.StorageSteps;
import org.apache.drillui.test.framework.testng.unsecure.BaseUnsecureTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class StoragePageTest extends BaseUnsecureTest {

  @BeforeClass
  public void setupStoragePlugins() {
    RestBaseSteps.setupStoragePlugins();
  }

  @BeforeMethod
  public void navigateStorage() {
    NavigationSteps.navigateStorage();
  }

  @Test(groups = {"functional"})
  public void pluginsPresented() {
    String[] plugins = {
        "cp", "dfs", "hbase", "hive", "kafka", "kudu", "mongo", "opentsdb", "s3"
    };
    for (String plugin : plugins) {
      assertTrue(StorageSteps.exists(plugin),
          String.format("The plugin \"%s\" is not present!", plugin));
    }
  }

  @Test(groups = {"functional"})
  public void pluginsEnabled() {
    String[] plugins = {
        "cp", "dfs"
    };
    for (String plugin : plugins) {
      assertTrue(StorageSteps.enabled(plugin),
          String.format("The plugin \"%s\" is not enabled!", plugin));
    }
  }

  @Test(groups = {"functional"})
  public void enableDisablePlugin() {
    String pluginTested = "dfs";
    if (!StorageSteps.enabled(pluginTested)) {
      StorageSteps.enable(pluginTested);
    }
    StorageSteps.disable(pluginTested);
    assertFalse(StorageSteps.enabled(pluginTested));
    StorageSteps.enable(pluginTested);
    assertTrue(StorageSteps.enabled(pluginTested));
  }
}
