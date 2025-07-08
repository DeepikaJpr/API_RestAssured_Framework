
Feature: Verify order placing functionality for ECommerce website
  
  Background: login
    Given "_loginPayload" API payload
    When send "POST" request with "_loginAPIResource"
    Then response "message" is equals to "Login Successfully" for "_loginTest"


  Scenario: Create Product
    Given "_createProductPayload" API payload
    When send "POST" request with "_createProductAPIResource"
    Then response "message" is equals to "Product Added Successfully" for "_createProductTest"

 Scenario: Delete Order
 		Given "_createProductPayload" API payload
    When send "POST" request with "_createProductAPIResource"
    Then response "message" is equals to "Product Added Successfully" for "_createProductTest"
    
 		Given "_deleteProductPayload" API payload
    When send "DELETE" request with "_deleteProductAPIResource"
    Then response "message" is equals to "Product Deleted Successfully" for "_deleteProductTest"