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
package org.apache.drill_web_test_framework.rest_api.tests.unsecure;

import org.apache.drill_web_test_framework.properties.PropertiesConst;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;

public class ClusterTest extends BaseRestTest {
  @Test
  public void getClusterJson() {
    given()
        .filter(sessionFilter)
        .when()
        .get("/cluster.json")
        .then().statusCode(200)
        .body("drillbits.userPort", hasItem("31010"))
        .body("drillbits.controlPort", hasItem("31011"))
        .body("drillbits.dataPort", hasItem("31012"))
        .body("drillbits[0].current", equalTo(true))
        .body("drillbits.versionMatch", everyItem(equalTo(true)))
        .body("currentVersion", equalTo(PropertiesConst.DRILL_VERSION))
        .body("userEncryptionEnabled", equalTo(false))
        .body("bitEncryptionEnabled", equalTo(false))
        .body("authEnabled", equalTo(PropertiesConst.SECURE_DRILL));
  }
}
