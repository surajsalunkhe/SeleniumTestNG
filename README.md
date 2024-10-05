# Selenium Cucumber Test Automation Framework
Test Automation Framework for Build Testing

<h3>Project Description:</h3>
- Project have implemented to Automate the scenarios of Any Website Test Automation
- This project illustrate design of Hybrid Framework with Page Object Model (POM) with Cucumber BDD & Selenium.

<h3>Project Component:</h3>
- Driver Manager Library - Initialize required ThreadLocal WebDriver depends on browser.
- Component Library      - Provide all required element wrapper methods.
- Element Utility        - Provide all required element action wrapper utility.
- PropertiesFileManager  - Library that read properties from property file and store value in file
- Util library           - Excel Reader(Read Excel,Write Excel), Send Test Automation Report, Test Data Generator,


    feature Files              : Cucumber Feature file holds all required Cucumber feature file.
                                 /src/test/resources/features
                 
    Step Defination           : Holds respective step definations class.
                                /src/test/java/stepDef
                                
    Page Library              : Page Classes for POM
                                /src/main/java/com/org/pages
                                
                                
    Constant Library           : All input data  to be tried out by various test methods woulbe be going into this directory
                                 /src/main/java/com/org/util/Constants.java
                                
    Application Hooks         : Cucumber Hooks with before and after
                                /src/test/java/appHooks/ApplicationHook.java
                                
    Config                    : Holds input data that needs to be pass to Test Method.
                                /src/test/resources/environment/qa/environment.properties
                                /src/test/resources/environment/stage/environment.properties
                                
    Test Runner               : Test Runners in TestNG
                                /src/test/java/TestRunner/TestRunner.java
                                
    Reporting Property        : All required input to generate HTML Extent Spark and PDF Report with Test Method results.
                                /src/test/resources/extent.properties
                                /src/test/resources/extent-config.xml
                                /src/test/resources/cucumber.properties
    
    Utility                 :  All required utility for Constant Reading,Read Data From Excel, Read Properties File, Element Util for Selenium Functions
                               /src/main/java/com/org/util/ExcelReader.java
                               /src/main/java/com/org/util/ElementUtil.java
                               /src/main/java/com/org/util/DataManager.java
                               /src/main/java/com/org/util/ConfigPropertyReader.java
                               /src/main/java/com/org/util/CommonUtility.java
                               /src/main/java/com/org/util/Constants.java
                               src/main/java/com/org/util/PropertiesFileManager.java

    Jira Update              : From environment.properties jira update can be enabled or disabled. You can configure jira org url with username and access key

    SendTestAutomationReport : From environment.properties Sending email report can be enabled or disabled. You can configure individual or group DL mail from src/test/java/utils/SendTestAutomationReport

    Parallel Execution       : From Pom.xml increase for the value dataproviderthreadcount of field the number of thread 

Technologies Used:

     1. Selenium WebDriver 4+ with Java Language binding
     2. Cucumber 6.x JVM library
     3. WebDriverManager
     4. JDK 1.8
     5. Maven (Build tool)
     6. Maven Plugins
     7. Cucumber extent report 6 adapter
     8. TestNG 7 baove 4.x library
     9. IntelliJIDEA (IDE)
     10. APACHE POI 

<h3>Execution:</h3>

- To Execute All the Feature file navigate to /src/test/java/testRunner/TestRunner.java class and execute with help of TestNG.
- To execute specific Feature file, mention the required feature file in TestRunner.java class and run with TestNG.
- To Execute on Maven   ```mvn clean verify -Denv=UAT -Dbrowser=chrome```
  To Run on specified enviornemnt with Assertion from env folder File.
  ```mvn clean test -Denv=UAT -Dbrowser=chrome -Dtest=TestRunner "-Dcucumber.filter.tags=@Smoke" -f pom.xml```
  User can specify browser to run TC else default chrome will be chosen for TC automation.
  -Run script by right click on feature file.  

Reference Link:

<br>Setup Maven:https://mkyong.com/maven/how-to-install-maven-in-windows/</br>
<br>Setup Java:https://www.geeksforgeeks.org/setting-environment-java/</br>