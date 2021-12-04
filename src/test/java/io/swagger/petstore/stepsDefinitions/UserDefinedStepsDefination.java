package io.swagger.petstore.stepsDefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.swagger.petstore.qaUtils.ConfigFileReader;
import io.swagger.petstore.qaUtils.FakeData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import static io.restassured.RestAssured.given;

public class UserDefinedStepsDefination {
    private ConfigFileReader configReader = new ConfigFileReader();
    private RequestSpecification requestSpec = given().baseUri(configReader.property("uri"));
    private Response response;
    private String requestBody;
    private ArrayList<Response> responseMapList = new ArrayList<Response>();
    private ArrayList<HashMap<Object, Object>> requestMapList = new ArrayList<HashMap<Object, Object>>();
    private Logger log = LogManager.getLogger();

    @Given("^I Perform get operation for (login|logout) url$")
    public void performGetOperation(String url, DataTable params) throws Throwable {
        log.info("Executing Step - I Perform get operation for " + url);
        url = "/user/" + url;
        Response response;
        List<Map<String, String>> data = params.asMaps(String.class, String.class);
        Map<String, String> dataMap = data.get(0);
        System.out.println(dataMap);
        if (url.contains("logout")) {
            response = requestSpec.get(url);
        } else {
            requestSpec.queryParams(dataMap);
            response = requestSpec.get(url);
        }
        log.info(response.asPrettyString());
        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 status code but found " + response.getStatusCode());
        log.info("End of Step - I Perform get operation for " + url);
    }

    @Given("^I create a new (store order|single user|multiple user|pet) with url \"([^\"]*)\" and (.*) variable$")
    public void performCreateOperation(String creationType, String url, String variableName, DataTable table) throws Throwable {
//        Response response;
        //String requestBody;
        this.requestBody = "";
        List<Map<String, String>> createStoreMap = table.transpose().asMaps();
        if (!creationType.equalsIgnoreCase("pet")) {
            HashMap<Object, Object> updatedDataMap = new HashMap<Object, Object>();
            ArrayList<HashMap<Object, Object>> dataMapList = new ArrayList<HashMap<Object, Object>>();
            for (Map<String, String> map : createStoreMap) {
                updatedDataMap = FakeData.generateDataFromHashMap(map);
                dataMapList.add(updatedDataMap);
            }
            if (dataMapList.size() > 1) {
                List<JSONObject> jsonObj = new ArrayList<JSONObject>();

                for (HashMap<Object, Object> data : dataMapList) {
                    JSONObject obj = new JSONObject(data);
                    jsonObj.add(obj);
                    requestMapList.add(data);
                }
                this.requestBody = jsonObj.toString();
                log.info("****************************************************************************");
                log.info("Request JSON created for CreateUserList operation is " + jsonObj.toString());
                log.info("****************************************************************************");
            } else {
                JSONObject json = new JSONObject(updatedDataMap);
                this.requestBody = json.toString();
                log.info("****************************************************************************");
                log.info("Request JSON created for Create" + creationType + " operation is " + this.requestBody);
                log.info("****************************************************************************");
            }
        } else {
            HashMap<Object, Object> updatedDataMap = new HashMap<Object, Object>();
            updatedDataMap = FakeData.generateDataFromHashMap(createStoreMap.get(0));
            String requestBody = "{\r\n" +
                    "  \"id\": " + updatedDataMap.get("id") + ",\r\n" +
                    "  \"name\": \"" + updatedDataMap.get("name") + "\",\r\n" +
                    "  \"category\": {\r\n" +
                    "    \"id\": " + updatedDataMap.get("category_id") + ",\r\n" +
                    "    \"name\": \"" + updatedDataMap.get("category_name") + "\"\r\n" +
                    "  },\r\n" +
                    "  \"photoUrls\": [\r\n" +
                    "    \"" + updatedDataMap.get("photoUrls") + "\"\r\n" +
                    "  ],\r\n" +
                    "  \"tags\": [\r\n" +
                    "    {\r\n" +
                    "      \"id\": " + updatedDataMap.get("tags_id") + ",\r\n" +
                    "      \"name\": \"" + updatedDataMap.get("tags_name") + "\"\r\n" +
                    "    }\r\n" +
                    "  ],\r\n" +
                    "  \"status\": \"" + updatedDataMap.get("status") + "\"\r\n" +
                    "}";

            this.requestBody = requestBody;
        }
//        response = postOpsWithBodyParams(url, this.requestBody);
        this.response = requestSpec.contentType(ContentType.JSON).body(this.requestBody).post(url);
        log.info("****************************************************************************");
        log.info("***Response "+this.response +"***");
        log.info("****************************************************************************");
        System.out.println(this.response.asPrettyString());
    }

    @Then("^I verify response details below$")
    public void i_verify_response_details_below(DataTable table) throws ParseException {
        List<Map<String, String>> createStoreMap = table.transpose().asMaps(String.class, String.class);
        JSONParser parser = new JSONParser();
        JSONObject expectJSONObject = null;
        JSONObject actualJSONObject = null;
        if (createStoreMap.size() > 1) {
            for (Map<String, String> map : createStoreMap) {
                expectJSONObject = new JSONObject(map);
                actualJSONObject = (JSONObject) parser.parse(this.response.prettyPrint());
                for (Object key : expectJSONObject.keySet()) {
                    String expected = String.valueOf(expectJSONObject.get(key));
                    String actual = String.valueOf(actualJSONObject.get(key));
                    Assert.assertTrue(actual.equalsIgnoreCase(expected), "Expected Response value for " + key + " is: " + expected + " But Actual is: " + actual);
                    log.info("Value is correct for " + key + ": " + expected);
                }
            }

        } else {

            expectJSONObject = new JSONObject(createStoreMap.get(0));
            actualJSONObject = (JSONObject) parser.parse(this.response.prettyPrint());

            for (Object key : actualJSONObject.keySet()) {
                String expected = String.valueOf(expectJSONObject.get(key));
                String actual = String.valueOf(actualJSONObject.get(key));
                if (key.toString().contains("date")) {
                    Assert.assertTrue(actual.contains(expected), "Expected Response value for " + key + " is: " + expected + " But Actual is: " + actual);
                } else {
                    Assert.assertTrue(actual.equalsIgnoreCase(expected), "Expected Response value for " + key + " is: " + expected + " But Actual is: " + actual);
                }
                log.info("Value is correct for " + key + ": " + expected);
            }
        }
    }

    @Given("^I perform (get|delete) operation to (fetch|delete) (order|user|pet) details for url \"([^\"]*)\" with (.*) variable$")
    public void getOrderDetails(String operation, String action, String type, String url, String varName) throws Throwable {
        response = null;
        JsonPath jPath = new JsonPath(this.requestBody);
        if (url.contains("order")) {
            url = url + jPath.get("id");
            log.info("URL is " + url);
            if (operation.contains("delete")) {
                this.response = requestSpec.delete(url);
            } else {
                this.response = requestSpec.get(url);
            }
        } else if (url.contains("pet")) {
            url = url + jPath.get("id");
            log.info("URL is " + url);
            if (operation.contains("delete")) {
                requestSpec.header("api_key", configReader.property("apiKey"));

                this.response = requestSpec.delete(url);
            } else {
                this.response = requestSpec.get(url);
            }
        } else {
            url = url + varName.trim();
            log.info("URL is " + url);
            if (operation.contains("delete")) {
                this.response = requestSpec.delete(url);
            } else {
                this.response = requestSpec.get(url);
            }
        }


        log.info("****************************************************************************");
        log.info("Response JSON created for " + operation + " " + "type" + " operation is " + this.response.asPrettyString());
        log.info("****************************************************************************");
        System.out.println(this.response.asPrettyString());
    }

    @Then("I verify response has {string} with status code {int}")
    public void i_verify_response_has_with_status_code(String message, Integer code) {
        JsonPath jsonPath = new JsonPath(this.response.prettyPrint());
        JsonPath jsonReqPath = new JsonPath(this.requestBody);
        Assert.assertTrue(this.response.getStatusCode() == code, "Expected status code: " + code + " Actual: " + this.response.getStatusCode());
        if (!message.isEmpty()) {
            Assert.assertEquals(jsonPath.get("message"), message, "Expected Error Message: " + message + " Actual: " + jsonPath.get("message"));
        }

    }

    @Given("^I perform put operation for url \"([^\"]*)\" with (.*) variable updating (.*) as (.*)$")
    public void performUpdateUserOperation(String url, String varName, String key, String value) throws Throwable {
        response = null;
        if (!url.contains("pet")) {
            url = url + varName.trim();
        }
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(this.requestBody);
        json.put(key, value);
        log.info("updated body " + json);
        this.requestBody = json.toString();
        log.info("String body " + this.requestBody);
        this.response = requestSpec.contentType(ContentType.JSON).body(this.requestBody).put(url);
        log.info("****************************************************************************");
        log.info("Response JSON created for PUT USER operation is " + response.asPrettyString());
        log.info("****************************************************************************");
    }

    @Then("I verify response details")
    public void i_verify_response_details() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject requestjson = null;
        JSONObject responsejson = null;
        requestjson = (JSONObject) parser.parse(this.requestBody);
        responsejson = (JSONObject) parser.parse(this.response.asPrettyString());

        for (Object key : responsejson.keySet()) {
            String expected = String.valueOf(requestjson.get(key));
            String actual = responsejson.get(key).toString();
            if (key.toString().contains("Date")) {
                Assert.assertTrue(actual.contains(expected), "Expected Response value for " + key + " is: " + expected + " But Actual is: " + actual);
            } else {
                Assert.assertTrue(actual.equalsIgnoreCase(expected), "Expected Response value for " + key + " is: " + expected + " But Actual is: " + actual);
            }
            log.info("Value is correct for " + key + ": " + expected);
        }
    }

    @Given("^I perform get operation to fetch inventory details for url \"([^\"]*)\"$")
    public void getInventoryDetailsById(String url) throws Throwable {
        this.response = requestSpec.get(url);
        log.info("Response ---> " + response.prettyPrint());
    }

    @Given("^I perform get operation to fetch (order|pet) details for url \"([^\"]*)\" with (orderId|tags|status|petId) value (.*)$")
    public void getOrderDetailsById(String type, String url, String attribute, String variableName) throws Throwable {
        if (url.contains("findBy")) {
            this.requestSpec.queryParam(attribute, variableName);
            this.response = this.requestSpec.get(url);
        } else {
            url = url + variableName;
            this.response = requestSpec.get(url);
            log.info("Response --> " + this.response.asPrettyString());
        }
    }

    @Given("^I verify response received has (.*)$")
    public void verifyResponseValues(String messageList) throws Throwable {
        messageList = messageList.replaceAll("\"", "");
        for (String message : messageList.split("\\|")) {
            Assert.assertTrue(this.response.asPrettyString().contains(message), "Expected Error Message: " + message + " Actual: " + this.response.asPrettyString());

        }
    }

    @Given("^I perform post operation for url \"([^\"]*)\" with (.*) variable updating name as (.*) and status as (.*)$")
    public void performUpdateUserFormDataOperation(String url, String userName, String name, String status) throws Throwable {
        response=null;
        JsonPath jPath = new JsonPath(this.requestBody);
        url = url + jPath.get(userName);
        HashMap<String, String> queryParamMap = new HashMap<String, String>();
        queryParamMap.put("name", name);
        queryParamMap.put("status", status);
        JSONParser parser = new JSONParser();
        JSONObject requestjson = (JSONObject) parser.parse(this.requestBody);
        requestjson.put("name", name);
        requestjson.put("status", status);
        this.requestBody = requestjson.toString();
        response = requestSpec.body(requestBody).post(url);
       // response = postWithQueryParams(url, queryParamMap);
        log.info("****************************************************************************");
        log.info("Response JSON created for PUT USER operation is " + response.asPrettyString());
        log.info("****************************************************************************");
    }

    @Given("^I verify response with status code (.*)$")
    public void verifyResponseValues(int code) throws Throwable {
        Assert.assertTrue(this.response.getStatusCode() == code, "Expected status code: " + code + " Actual: " + this.response.getStatusCode());

    }
    @When("I perform post operation for {string} with {string} to upload the file {string} addtional {string} data")
    public void i_perform_post_operation_for_to_upload_the_file_addtional_data(String url, String varName, String location, String metaData) {
        response = null;
        url = url+varName+"/uploadImage";
        log.info("******URL for uploadImage "+url+" *************************");
        File image = new File(location);
        response = requestSpec.contentType(ContentType.JSON)
                .contentType(ContentType.MULTIPART)
                .multiPart("additionalMetadata",metaData)
                .multiPart(image)
                .post("pet/4/uploadImage");
        log.info("*********************************************");
        log.info("********************"+response.asPrettyString()+"*************************");
        log.info("*********************************************");
    }

}
