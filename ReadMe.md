restassured-swagger-petstore-automation
=================
Automated API test for REST API using Java, RestAssured, Maven with Cucumber etc.

This test automation project is created for an open API. The tests are implemented user rest-assured.
I have used the BDD approach for this project.

Framework Architecture
--------------
	Project-Name
		|
		|_src/main/java
		|_src/test/java/io/swagger/petstore	
		|	|_qaUtils
		|	|   |_ConfigFileReader.java
		|	|   |_FakeData.java
		|	|_runner
		|	|   |_RunnerTest.java
		|	|_stepdefs
		|	|   |_UserDefinedStepsDefinition.java
		|
		|_src/test/resources
		|	|_featureFiles
		|	|   |_Pets.feature
		|	|   |_Store.feature
		|	|   |_User.feature
		|	|	
		|	|_pictures	
		|	|	|_cat.png
		|	|
		|	|_config.properties
		|	|_extent.properties
		|	|_log4j2.properties
		|
		|_testReport
			|_cucumber.html
			|_Spark.html

* **src/test/resources/featureFiles** - all the cucumber features files (files .feature ext) goes here.
* **src/test/java/io/swagger/petstore/stepsDefinitions** - you can define step defintion under this package for your feature steps.
* **src/test/resources/config.properties** - When you want to manage configuration for your API for example: like change in Base URI, Base path or authorization key or token. These could be managed from config.properties file.
* **src/test/resources/extent.properties** - When you want to manage extent report configuration.
* **src/test/resources/log4j2.properties** - When you want to manage logging configuration.

Writing a test
--------------
* The cucumber features goes in the `featuresFiles` library and should have the ".feature" extension.
* You can start out by looking at `featuresFiles/User.feature`. You can extend this feature or make your own features using some of the predefined steps that come with cucumber.
* You can use the existing step for new scenarios.

Proposed Test Cases & it's location:
--------------

	Resource_EndPoint-Name
	|
	|_Pet.feature
	|   |_Verify all Pet Operations
	|		|_Verify Create Pet operation
	|		|_Verify Put Pet operation
	|		|_Verify Delete Pet operation
	|		|_Verify Invalid Pet ID Scenario
	|		|_Verify Pet Not Found Scenario
	|		|_Verify updating Pet details with form data
	|		|_Verify Get Pet details by tags
	|		|_Verify Get Pet details by status
	|		|_Verify Upload Image operation
	|
	|_Store.feature
	|	|_Verify all Store Operations
	|		|_Verify Create Store operation
	|		|_Verify Invalid ID supplied Scenario
	|		|_Verify Order Not Found Scenario
	|		|_Verify get pet Inventories by status Scenario
	|		|_Verify Delete Order operation
	|		
	|_User.feature
	|	|_Verify all User operations
	|		|_Verify Get User Login/Logout operations
	|		|_Verify Create User operation
	|		|_Verify CreateList User operation
	|		|_Verify Delete User operation.
	|		|_Verify Put User operation

* The test cases are in "src/test/resources/featureFiles" directory, each feature file is related to each resource endpoint.
* Each feature file contains multiple scenarios for the given resource endpoint as per the above-given structure.

Execution from CMD
-----------------
* git clone https://github.com/irfanahmad7006/swagger-petstore.git
* cd swagger-petstore
* Run `mvn clean test` in cmd prompt for execution.

Execution from IDE
-----------------  
* You can start execution from `featuresFiles/User.feature` directly for single feature execution.
* You can start execution from `src/test/java/io/swagger/petstore/runner/RunnerTest.java` for all feature files execution in one go.
* You can run `mvn test` Or `mvn clean test` in cmd for execution from the terminal.


Reporting
----------------- 
* You can view the test execution report from `testReport/cucumber.html`
* You can view detailed test execution report from `testReport/Spark.html`
* You can also view the cucumber cloud report (only available for 24 hours) from the console, after every execution. This can be managed from cucumber options.


Set-Up prerequisites.
-----------------
* Java version - jdk 1.8.0_281,
* Maven version - apache maven 3.8.1,      
* maven-surefire-plugin - 3.0.0-M1 (If not provided by IDE on compile time)
* Git
* IntelliJ/Eclipse Cucumber plugins

Maven Dependencies.
-----------------
* cucumber-java
* cucumber-testNG
* testNG
* rest-assured
* extentreports
* extentreports-cucumber6-adapter
* logj42
* json-simple

Plans for future expansion.
-----------------
* Schema validation could be implemented.
* Jenkins integration.
* POJO to JSON Object implementation can be done (if required)
* Timeout validation could be implemented.
* `Note:` This API is an open API hence you may expect a couple of test case failures as it resets the data automatically. Therefore assertion comes in the picture due to automated data reset.