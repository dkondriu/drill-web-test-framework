# Test Framework for Apache Drill Web UI
Test Framework for [Apache Drill](http://drill.apache.org/) Web UI and Drill [REST API](https://drill.apache.org/docs/rest-api-introduction/) testing.

The test framework contains tests for browser testing of Drill Web UI using [Selenium Web driver](https://www.seleniumhq.org/projects/webdriver/).
Also the test framework covers Drill REST API testing using [REST Assured](http://rest-assured.io/). The main language for this test framework is Java.

## Requirements
1. OS with GUI (Windows, Linux, Mac OS).
2. JDK v8.0 or higher.
3. [Apache Maven](https://maven.apache.org/) v3.3.9 or higher.
4. Web browser to test with.
      Supported Web browsers: Windows (Chrome, Firefox, Edge), Linux (Chrome, Firefox), Mac OS (Chrome, Firefox)
5. Apache Drill instance is running (either in distributed or in embedded mode). It is not required to be run at the same system, all is needed is an access to the Web UI.

## Configuration
Test configuration can be set either in config file \(conf/init.properties\) or by adding a java option to the command. Example config is added to the config directory [init.properties.example](conf/init.properties.example). 

## Build Project
As the project contains only test classes, building it is not required.

## Execute Tests
Currently Maven is used to run tests. In the future it will be changed. Command syntax:
`mvn clean test -Dsurefire.suiteXmlFiles=<suites> <test parameters>`

 - \<suites\> - comma separated [TestNG xml file names](/testSuites) with test cases.
 - \<test parameters\> - space separated test parameters with `-D` in the beginning of every parameter. You can find available test configuration parameters in the example config file [init.properties.example](conf/init.properties.example)

**Example:**

`mvn clean test -Dsurefire.suiteXmlFiles=testSuites/unsecure_testng.xml, -DSECURE_DRILL=true`

## Contributing
Fill free to contribute this test framework! You can fix bugs or add new tests, enhancements. To add your improvements to the test framework please create a PR on GitHub for your change.

Refer to [CONTRIBUTING.md](CONTRIBUTING.md) for details on the test framework structure and instructions on how to contribute.

## License
Licensed under the Apache License 2.0. Please see [LICENSE.md](LICENSE.md)
