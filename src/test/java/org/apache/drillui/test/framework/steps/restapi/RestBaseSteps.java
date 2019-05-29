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
package org.apache.drillui.test.framework.steps.restapi;

import io.restassured.RestAssured;
import org.apache.commons.io.IOUtils;
import org.apache.drillui.test.framework.initial.PropertiesConst;
import org.apache.drillui.test.framework.initial.TestProperties;

import java.io.InputStream;
import java.nio.charset.Charset;

public final class RestBaseSteps {

  private static boolean storagePluginsSetupComplete = false;

  private RestBaseSteps() {
  }

  public static void setupStoragePlugins() {
    if (storagePluginsSetupComplete) {
      return;
    }
    boolean status;
    setupREST();
    status = RestStorageSteps.updateStoragePlugin(
        getDefaultDfsPlugin(),
        RestSecuritySteps.getDefaultSession());
    status = status && RestStorageSteps.updateStoragePlugin(
        getOpenTSDBPlugin(),
        RestSecuritySteps.getDefaultSession()
    );
    if (!status) {
      throw new RuntimeException("Some of the plugins did not updated!");
    }
    storagePluginsSetupComplete = true;
  }

  public static void setupREST() {
    RestAssured.baseURI = PropertiesConst.DRILL_HOST;
    RestAssured.port = PropertiesConst.DRILL_PORT;
  }

  private static String getPluginFromResource(String resource) {
    try (InputStream file = RestBaseSteps.class.getClassLoader()
        .getResourceAsStream(resource)) {
      return IOUtils.toString(file, Charset.defaultCharset());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String getDefaultDfsPlugin() {
    String pluginPath = PropertiesConst.DISTRIBUTED_MODE ?
        "restapi/storage/plugins/DfsDefaultMapRFS.json" :
        "restapi/storage/plugins/DfsDefaultEmbedded.json";
    return getPluginFromResource(pluginPath);
  }

  private static String getOpenTSDBPlugin() {
    return getPluginFromResource("restapi/storage/plugins/OpenTSDB.json");
  }
}
