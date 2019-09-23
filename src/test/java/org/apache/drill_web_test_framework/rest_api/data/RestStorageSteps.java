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
package org.apache.drill_web_test_framework.rest_api.data;

import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.getStringFromResource;
import static org.apache.drill_web_test_framework.rest_api.data.RestBaseSteps.setupREST;

public final class RestStorageSteps {
  private static boolean storagePluginsSetupComplete = false;
  private static HashMap<String, String> storagePluginsOriginal = new HashMap<>();

  private RestStorageSteps() {
  }

  public static boolean updateStoragePlugin(String body, SessionFilter session) {
    backupPlugin(new JSONObject(body).getString("name"));
    Response r = given()
        .filter(session)
        .body(body)
        .with()
        .contentType("application/json")
        .when()
        .post("/storage/plugin.json")
        .then()
        .extract()
        .response();
    return r.statusCode() == 200;
  }

  public static void backupPlugin(String pluginName) {
    storagePluginsOriginal.putIfAbsent(pluginName, getStoragePlugin(pluginName));
  }

  public static void restorePlugins() {
    storagePluginsOriginal.forEach((k, v) ->
        RestStorageSteps.updateStoragePlugin(v, RestSecuritySteps.getDefaultSession())
    );
  }

  private static String getStoragePlugin(String pluginName) {
    setupREST();
    return given()
        .filter(RestSecuritySteps.getDefaultSession())
        .when()
        .get("/storage/" + pluginName + ".json")
        .getBody()
        .asString();
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

  private static String getDefaultDfsPlugin() {
    String pluginPath = PropertiesConst.DISTRIBUTED_MODE ?
        "restapi/storage/plugins/DfsDefaultMapRFS.json" :
        "restapi/storage/plugins/DfsDefaultEmbedded.json";
    return getStringFromResource(pluginPath);
  }

  private static String getOpenTSDBPlugin() {
    return getStringFromResource("restapi/storage/plugins/OpenTSDB.json");
  }
}
