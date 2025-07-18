package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.testng.Assert;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import ECommerce_pojo.LoginRequest;
import ECommerce_pojo.LoginResponse;
import Utilities.APIResource;
import Utilities.JsonPathUtil;
import Utilities.PropReadUtil;
import Utilities.SpecsUtil;
import Utilities.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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
	Object productId;

	String addProductResponse;

	Properties prop;

	@Given("Login {string} API payload")
	public void login_api_payload(String payloadType) throws IOException {
		System.out.println("****GIVEN LOGIN******" + payloadType + "**********");

		if (TestContext.getToken() != null && TestContext.getUserId() != null) {
			System.out.println("Login already performed via Hooks. Skipping login step.");
			return;
		}

		PropReadUtil prUtil = new PropReadUtil();
		prop = prUtil.readProp();

		lreq = new LoginRequest();
		lreq.setUserEmail(prop.getProperty("ecomLogin"));
		lreq.setUserPassword(prop.getProperty("ecomPassword"));

		SpecsUtil sutil = new SpecsUtil();
		login_reqSpecs = sutil.getBaseSpec();
		given_result = given().spec(login_reqSpecs).body(lreq);
	}

	@Given("Create Product {string} API payload")
	public void create_product_api_payload(String payloadType) throws IOException {
		System.out.println("****GIVEN CREATE PRODUCT******" + payloadType + "**********");

		SpecsUtil sutil = new SpecsUtil();
		RequestSpecification authSpec = sutil.getAuthSpec(TestContext.getToken());

		File file = new File("src/test/resources/Screenshot_1.png");
		given_result = given().spec(authSpec)
				.header("Content-Type","multipart/form-data")
				.multiPart("productImage", file)
				.formParam("productName", "Test123")
				.formParam("productAddedBy",TestContext.getUserId())
				.formParam("productCategory", "fashion")
				.formParam("productSubCategory", "shirts")
				.formParam("productPrice", "9999")
				.formParam("productDescription", "Adidas Originals")
				.formParam("productFor", "women");
	}

	@Given("Delete Order {string} API payload")
	public void delete_order_api_payload(String payloadType) throws IOException {
	    System.out.println("****GIVEN DELETE PRODUCT******" + payloadType + "**********");

	    SpecsUtil sutil = new SpecsUtil();
	    RequestSpecification authSpec = sutil.getAuthSpec(TestContext.getToken());

	    given_result = given().spec(authSpec);
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
			System.out.println("``````````````" + given_result + "``````````");
			Object productId = TestContext.getProductId();
			when_response = given_result.pathParam("productId", productId).when().delete(_resourceAPI);
			break;
		default:
			System.out.println("=======INVALID API======");
			break;
		}

	}

	@Then("response {string} is equals to {string} for {string}")
	public void response_is_equals_to_for(String _key, String _value, String functionalityType) {

		System.out.println(_key + "********THEN*********" + _value + " for " + functionalityType);
		JsonPathUtil js = new JsonPathUtil();

		switch (functionalityType) {
		case "_loginTest":
			
			//	✅ Step 1: Schema validation
			when_response.then().assertThat().body(matchesJsonSchemaInClasspath("schema/login-schema.json"));
			
			// ✅ Step 2: Deserialize after validation
			lres = when_response.then().extract().response().as(LoginResponse.class);

			// ✅ Step 3: Token and UserId extraction
			TestContext.setToken(lres.getToken());
			token = TestContext.getToken();

			TestContext.setUserId(lres.getUserId());
			userId = TestContext.getUserId();

			// ✅ Step 4: Assertions
			System.out.println("Token: " + token);
			System.out.println("UserId: " + userId);
			System.out.println("Message: " + lres.getMessage());
			Assert.assertTrue(lres.getMessage().equals(_value),"Expected message does not match actual");
			break;

		case "_createProductTest":
			addProductResponse = when_response.then().extract().response().asString();
//			JsonPath jsCreate = new JsonPath(addProductResponse);
//			
//			TestContext.setProductId(jsCreate.getString("productId"));

			productId = js.rawToJson(addProductResponse, "productId");
			TestContext.setProductId((String) js.rawToJson(addProductResponse, "productId"));
			productId = TestContext.getProductId();

			System.out.println("ProductId: " + productId);
			System.out.println("Message: " + js.rawToJson(addProductResponse, _key));
			String actualValue = (String) js.rawToJson(addProductResponse, _key);

			assertEquals(actualValue, _value);
			break;

		case "_deleteProductTest":
			String deleteResponse = when_response.then().extract().response().asString();

			System.out.println("Delete Message: " + js.rawToJson(deleteResponse, _key));
			String actualDeleteValue = (String) js.rawToJson(deleteResponse, _key);
			assertEquals(actualDeleteValue, _value);

			break;

		default:
			throw new IllegalArgumentException("Invalid functionality type: " + functionalityType);
		}
	}

}
