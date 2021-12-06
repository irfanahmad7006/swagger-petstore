#Data Table having keywords GenerateNumber/GenerateString is then updated with dynamic values in stepdefinition
#For Pets request json, sample json is used as template however for Store and User jsons dynamic json gets created

Feature: Verify all User operations

  Scenario Outline: Verify Get User Login/Logout operations
    Given I Perform get operation for <urls> url
      | username | password |
      | test     | 123      |
    Examples:
      | urls   |
      | login  |
      | logout |

  Scenario: Verify Create User operation
    #Given I Perform post operation for url "/user" single user creation with John name
    Given I create a new single user with url "/user" and userName variable
      | id         | 1243                  |
      | username   | IrfanAhmad7006        |
      | firstName  | Irfan                 |
      | lastName   | Ahmad                 |
      | email      | irfan.ahmad@gmail.com |
      | password   | true                  |
      | phone      | 8802927006            |
      | userStatus | 1                     |
    Then I perform get operation to fetch user details for url "/user/" with IrfanAhmad7006 variable
    And I verify response details below
      | id         | 1243                  |
      | username   | IrfanAhmad7006        |
      | firstName  | Irfan                 |
      | lastName   | Ahmad                 |
      | email      | irfan.ahmad@gmail.com |
      | password   | true                  |
      | phone      | 8802927006            |
      | userStatus | 1                     |

  Scenario: Verify CreateList User operation
   # Given I Perform post operation for url "/user/createWithList" multiple user creation with theUser|TestUser name
    Given I create a new multiple user with url "/user/createWithList" and userName variable
      | id         | 1243                  | 1345                     |
      | username   | IrfanAhmad7006        | AbishekKumar             |
      | firstName  | Irfan                 | Abhishek                 |
      | lastName   | Ahmad                 | Kumar                    |
      | email      | irfan.ahmad@gmail.com | abhishek.kumar@gmail.com |
      | password   | true                  | true                     |
      | phone      | 8802927006            | 1234567890               |
      | userStatus | 1                     | 1                        |
    Then I perform get operation to fetch user details for url "/user/" with IrfanAhmad7006 variable
    And I verify response details below
      | id         | 1243                  |
      | username   | IrfanAhmad7006        |
      | firstName  | Irfan                 |
      | lastName   | Ahmad                 |
      | email      | irfan.ahmad@gmail.com |
      | password   | true                  |
      | phone      | 8802927006            |
      | userStatus | 1                     |
    Then I perform get operation to fetch user details for url "/user/" with AbishekKumar variable
    And I verify response details below
      | id         | 1345                     |
      | username   | AbishekKumar             |
      | firstName  | Abhishek                 |
      | lastName   | Kumar                    |
      | email      | abhishek.kumar@gmail.com |
      | password   | true                     |
      | phone      | 1234567890               |
      | userStatus | 1                        |

  Scenario: Verify Delete User operation.
    Given I create a new single user with url "/user" and userName variable
      | id         | 9909                          |
      | username   | Gryffindor                    |
      | firstName  | Albus                         |
      | lastName   | Dumbledore                    |
      | email      | albus.dumbledore@hogwarts.com |
      | password   | true                          |
      | phone      | 0099928489                    |
      | userStatus | 1                             |
    When I perform delete operation to delete user details for url "/user/" with Gryffindor variable
   #When I perform get operation to fetch user details for url "/user" with userName variable
    Then I perform get operation to fetch user details for url "/user/" with Gryffindor variable
    Then I verify response has "User not found" with status code 404

  Scenario: Verify Put User operation
    Given I create a new single user with url "/user" and userName variable
      | id         | 1102                      |
      | username   | Gryffindor1               |
      | firstName  | Harry                     |
      | lastName   | Potter                    |
      | email      | harry.potter@hogwarts.com |
      | password   | true                      |
      | phone      | 124577878                 |
      | userStatus | 1                         |
    When I perform put operation for url "/user/" with Gryffindor1 variable updating phone as 1234578
    Then I perform get operation to fetch user details for url "/user/" with Gryffindor1 variable
    And I verify response details below
      | id         | 1102                      |
      | username   | Gryffindor1               |
      | firstName  | Harry                     |
      | lastName   | Potter                    |
      | email      | harry.potter@hogwarts.com |
      | password   | true                      |
      | phone      | 1234578                   |
      | userStatus | 1                         |
 