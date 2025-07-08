package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import ECommerce_pojo.LoginRequest;
import ECommerce_pojo.LoginResponse;
import Utilities.APIResource;
import Utilities.PropReadUtil;
import Utilities.SpecsUtil;
import Utilities.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PlaceOrder_StepDef {

	RequestSpecification login_reqSpecs;
	RequestSpecification createProduct_reqSpecs;
	RequestSpecification deleteProduct_reqSpecs;

	LoginRequest lreq;
	LoginResponse lres;
	RequestSpecification given_result;

	Response when_response;
	String token;
	String userId;
	String productId;

	String addProductResponse;

	Properties prop;

	@Given("{string} API payload")
	public void api_payload(String payloadType) throws FileNotFoundException, IOException {

		System.out.println("****GIVEN******" + payloadType + "**********");

		// LOGIN TESTCASE GIVEN

		// Base Url and payload set in SpecsUtil , Using object to fetch

		// Reading critical details from properties like username and password
		PropReadUtil prUtil = new PropReadUtil();
		prop = prUtil.readProp();
		// prop.load(new FileInputStream("src/test/java/resources/config.properties"));

		// setting pojo using properties file details
		lreq = new LoginRequest();
		lreq.setUserEmail(prop.getProperty("ecomLogin"));
		lreq.setUserPassword(prop.getProperty("ecomPassword"));

		SpecsUtil sutil = new SpecsUtil();

		switch (payloadType) {
		case "_loginPayload":
			login_reqSpecs = sutil.getLoginReqSpec();
			given_result = given().log().all().spec(login_reqSpecs).body(lreq);
			break;
		case "_createProductPayload":
			createProduct_reqSpecs = sutil.getCreateProductReqSpec(token, userId,login_reqSpecs);
			given_result = given().log().all().spec(createProduct_reqSpecs);
			break;
		case "_deleteOrderPayload":
			deleteProduct_reqSpecs = sutil.getDeleteProductReqSpec(token,login_reqSpecs);
			given_result = given().log().all().spec(deleteProduct_reqSpecs);
			break;
		default:
			break;
		}

	}

	@When("send {string} request with {string}")
	public void send_request_with(String requestType, String resourceType) {

		System.out.println(requestType + "******WHEN********" + resourceType);
		String _resourceAPI = APIResource.valueOf(resourceType).getResource();

		switch (requestType) {
		case "POST":
			when_response = given_result.when().post(_resourceAPI);
			break;
		case "GET":
			when_response = given_result.when().get(_resourceAPI);
			break;
		case "DELETE":
			when_response = given_result.pathParam("productId", productId) .when().delete(_resourceAPI);
			break;
		default:
			System.out.println("=======INVALID API======");
			break;
		}

	}

	@Then("response {string} is equals to {string} for {string}")
	public void response_is_equals_to_for(String _key, String _value, String functionalityType) {
		System.out.println(_key + "********THEN*********" + _value + " for " + functionalityType);

		switch (functionalityType) {
		case "_loginTest":
			lres = when_response.then().extract().response().as(LoginResponse.class);
			
			TestContext.setToken(lres.getToken());
			token = TestContext.getToken();
			
			TestContext.setUserId(lres.getUserId());
			userId = TestContext.getUserId();
			
			System.out.println("Token: " + token);
			System.out.println("UserId: " + userId);
			System.out.println("Message: " + lres.getMessage());
			assertTrue("Expected message does not match actual", lres.getMessage().equals(_value));
			break;

		case "_createProductTest":
			addProductResponse = when_response.then().log().all().extract().response().asString();
			JsonPath jsCreate = new JsonPath(addProductResponse);
			
			TestContext.setProductId(jsCreate.getString("productId"));
			productId = TestContext.getProductId();
			
			productId = jsCreate.getString("productId");
			System.out.println("ProductId: " + productId);
			System.out.println("Message: " + jsCreate.getString(_key));
			assertTrue("Expected message does not match actual", jsCreate.getString(_key).equals(_value));
			break;

		case "_deleteProductTest":
			String deleteResponse = when_response.then().log().all().extract().response().asString();
			JsonPath jsDelete = new JsonPath(deleteResponse);
			System.out.println("Delete Message: " + jsDelete.getString(_key));
			assertTrue("Expected message does not match actual", jsDelete.getString(_key).equals(_value));
			break;

		default:
			throw new IllegalArgumentException("Invalid functionality type: " + functionalityType);
		}
	}

}
