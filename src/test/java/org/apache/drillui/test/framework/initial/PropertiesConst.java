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
package org.apache.drillui.test.framework.initial;

public class PropertiesConst {

  //Apache Drill cluster information
  public static final String DRILL_HOST = TestProperties.get("DRILL_HOST");
  public static final int DRILL_PORT = TestProperties.getInt("DRILL_PORT");
  public static final String DRILL_VERSION = TestProperties.get("DRILL_VERSION");

  //Can be distributed (true) or embedded (false)
  public static final boolean DISTRIBUTED_MODE = TestProperties.getBool("DISTRIBUTED_MODE");

  //Plain security with user/password authentication
  public static final boolean SECURE_DRILL = TestProperties.getBool("SECURE_DRILL");
  public static final String ADMIN_1_NAME = TestProperties.get("ADMIN_1_NAME");
  public static final String ADMIN_1_PASSWORD = TestProperties.get("ADMIN_1_PASSWORD");
  public static final String ADMIN_2_NAME = TestProperties.get("ADMIN_2_NAME");
  public static final String ADMIN_2_PASSWORD = TestProperties.get("ADMIN_2_PASSWORD");
  public static final String USER_1_NAME = TestProperties.get("USER_1_NAME");
  public static final String USER_1_PASSWORD = TestProperties.get("USER_1_PASSWORD");
  public static final String USER_2_NAME = TestProperties.get("USER_2_NAME");
  public static final String USER_2_PASSWORD = TestProperties.get("USER_2_PASSWORD");

  // Browser to use. Supported types: CHROME (default), FIREFOX, IE, EDGE
  public static final String DRIVER_TYPE = TestProperties.get("DRIVER_TYPE");

  // Timeout to load pages (in seconds)
  public static final int DEFAULT_TIMEOUT = TestProperties.getInt("DEFAULT_TIMEOUT");
}
