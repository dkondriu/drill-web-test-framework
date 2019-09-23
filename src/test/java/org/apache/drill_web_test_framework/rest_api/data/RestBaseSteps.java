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

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import org.apache.commons.io.IOUtils;
import org.apache.drill_web_test_framework.properties.PropertiesConst;

import java.io.InputStream;
import java.nio.charset.Charset;

import static io.restassured.RestAssured.given;

public final class RestBaseSteps {

  private RestBaseSteps() {
  }

  public static void setupREST() {
    RestAssured.baseURI = PropertiesConst.DRILL_HOST;
    RestAssured.port = PropertiesConst.DRILL_PORT;
  }

  public static String getStringFromResource(String resource) {
    try (InputStream file = RestBaseSteps.class.getClassLoader()
        .getResourceAsStream(resource)) {
      return IOUtils.toString(file, Charset.defaultCharset());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void runQueryInBackground(String query, SessionFilter sessionFilter) {
    new Thread(() -> runQuery(query, sessionFilter)).start();
  }

  public static void runQuery(String query, SessionFilter sessionFilter) {
    String queryText = "{\"queryType\":\"SQL\", \"query\": \"" + query + "\"}";
    given()
        .filter(sessionFilter)
        .body(queryText)
        .with()
        .contentType("application/json")
        .when()
        .post("/query.json")
        .then()
        .statusCode(200);
  }
}
