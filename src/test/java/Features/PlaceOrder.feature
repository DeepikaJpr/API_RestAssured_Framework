
Feature: Verify order placing functionality for ECommerce website
  
  Scenario: login
    Given "_loginPL" API payload
    When send "POST" request with "api/ecom/auth/login"
    Then response "message" is equals to "Login Successfully"


  #Scenario: Create Product
    #Given "_createProductPL" API payload
    #When send "POST" request with "api/ecom/product/add-product"
    #Then response "message" is equals to "Product Added Successfully"
    #
  #Scenario: Create Order
    #Given "_createOrderPL" API payload
    #When send "POST" request with "api/ecom/order/create-order"
    #Then response "message" is equals to "Product Added Successfully"
    #
 #Scenario: Order Details
 #		Given "_OrderDetailsPL" API payload
    #When send "GET" request with "api/ecom/order/get-orders-details"
    #Then response "message" is equals to "Orders fetched for customer Successfully"
    #
 #Scenario: Delete Order
 #		Given "_deleteOrderPL" API payload
    #When send "Delete" request with "api/ecom/product/delete-product/{productId}"
    #Then response "message" is equals to "Product Deleted Successfully"