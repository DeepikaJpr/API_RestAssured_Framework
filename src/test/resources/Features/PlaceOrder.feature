
Feature: Verify order placing functionality for ECommerce website
  
  @Login
  Scenario: login
    Given Login "_loginPayload" API payload
    When send "POST" request with "_loginAPIResource"
    Then response "message" is equals to "Login Successfully" for "_loginTest"

	@CreateProduct
  Scenario: Create Product
    Given Create Product "_createProductPayload" API payload
    When send "POST" request with "_createProductAPIResource"
    Then response "message" is equals to "Product Added Successfully" for "_createProductTest"

	@DeleteProduct
  Scenario: Delete Order
 		Given Delete Order "_deleteProductPayload" API payload
    When send "DELETE" request with "_deleteProductAPIResource"
    Then response "message" is equals to "Product Deleted Successfully" for "_deleteProductTest"