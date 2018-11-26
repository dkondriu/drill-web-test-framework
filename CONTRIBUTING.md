## Structure
 <pre><code>
   drill-webui-test-framework
     |_ conf
        |_ init.properties (config file)
     |_ src/test/java/org/apache/drillui/test/framework
        |_ initial 
        |_ pages (contains page objects)
        |_ steps (contains utility classes with test steps methods)
        |_ testng (contains tests)
     |_ testSuites (TestNG test suites in xml format)
     |_ webdrivers 
</code></pre>

## Main Concepts
**Webdriver** - a java program what runs a real instance of web browser and provides an API to control it. The webdriver is handled in [WebBrowser.java](src/test/java/org/apache/drillui/test/framework/initial/WebBrowser.java)

**Page Objects** - java classes that represent a web page and contain accessors to the page elements. They should not contain any additional logic.

**Steps** - utility classes that contain actions to perform against page objects. 

**Tests** - [TestNG](https://testng.org/doc/index.html) is used as the internal test framework. So tests are represented by methods with \@Test annotation. TestNG also has useful assertion methods and annotations to setup/teardown. Sequence of actions from the Steps classes is used as the test steps.

Overall dependencies between the framework elements looks like:

**Test --> Steps --> Page Object --> Webdriver**

## Adding Tests
1. Tests are in the [testng](src/test/java/org/apache/drillui/test/framework/testng) package. You can add new tests by adding them to existing classes or create a new class/package.
2. Tests should use methods from [steps](src/test/java/org/apache/drillui/test/framework/steps) package.
3. If steps are not contain necessary method, you could create new steps method using [pages](src/test/java/org/apache/drillui/test/framework/pages) methods.
4. If Drill Web UI was changed you could update Selenium locators in page classes to use it later in steps.
5. If you add new test class in the [testng](src/test/java/org/apache/drillui/test/framework/testng) package, this classe should be also added to an appropriate test suite in the [testSuites](/testSuites) directory.
