#Data Table having keywords GenerateNumber/GenerateString is then updated with dynamic values in stepdefinition
#For Pets request json, sample json is used as template however for Store and User jsons dynamic json gets created

Feature: Verify all Pet Operations

  Scenario: Verify Create Pet operation
    Given I create a new pet with url "/pet" and petId variable
      | id            | GenerateNumber |
      | name          | TestName14     |
      | category_id   | 1              |
      | category_name | Monkey         |
      | photoUrls     | imageUrl       |
      | tags_id       | GenerateNumber |
      | tags_name     | GenerateString |
      | status        | available      |
    When I perform get operation to fetch pet details for url "/pet/" with petId variable
    Then I verify response details

  Scenario: Verify Put Pet operation
    Given I create a new pet with url "/pet" and petId variable
      | id            | GenerateNumber |
      | name          | TestName1      |
      | category_id   | 2              |
      | category_name | PIG            |
      | photoUrls     | imageUrl       |
      | tags_id       | GenerateNumber |
      | tags_name     | GenerateString |
      | status        | available      |
    When I perform put operation for url "/pet/" with petId variable updating status as sold
    Then I perform get operation to fetch pet details for url "/pet/" with petId variable
    And I verify response details

  Scenario: Verify Delete Pet operation
    Given I create a new pet with url "/pet" and petId variable
      | id            | GenerateNumber |
      | name          | TestName2      |
      | category_id   | 2              |
      | category_name | Lion           |
      | photoUrls     | imageUrl       |
      | tags_id       | GenerateNumber |
      | tags_name     | GenerateNumber |
      | status        | available      |
    When I perform delete operation to delete pet details for url "/pet/" with petId variable
    Then I verify response has "" with status code 200

  Scenario: Verify Invalid Pet ID Scenario
    When I perform get operation to fetch pet details for url "/pet/" with petId value a
    Then I verify response has "java.lang.NumberFormatException: For input string: \"a\"" with status code 404
  #
  Scenario: Verify Pet Not Found Scenario
    When I perform get operation to fetch pet details for url "/pet/" with petId value 1500
    Then I verify response has "Pet not found" with status code 404

  Scenario: Verify updating Pet details with formdata
    Given I create a new pet with url "/pet" and petId variable
      | id            | GenerateNumber |
      | name          | TestName3      |
      | category_id   | 2              |
      | category_name | Cats           |
      | photoUrls     | imageUrl       |
      | tags_id       | GenerateNumber |
      | tags_name     | GenerateNumber |
      | status        | available      |
    When I perform post operation for url "/pet/" with id variable updating name as Rocky and status as sold
    Then I perform get operation to fetch pet details for url "/pet/" with petId variable
    And I verify response details

  Scenario: Verify Get Pet details by tags
    When I perform get operation to fetch pet details for url "/pet/findByTags" with tags value string
    Then I verify response with status code 200

  Scenario: Verify Get Pet details by status
    When I perform get operation to fetch pet details for url "/pet/findByStatus" with status value available
    Then I verify response with status code 200

  Scenario Outline: Verify Upload Image operation
    When I perform post operation for "<url>" with "<petID>" to upload the file "<fileLocation>" addtional "<metaData>" data
    Then I verify response with status code 200
    Examples:
      | petID | url   | fileLocation                        | metaData   |
      | 1     | /pet/ | src/test/resources/pictures/cat.png | testString |



 #http://localhost:8080/api/v3/user/login?username=test6&password=pasd
 