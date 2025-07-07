package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import ECommerce_pojo.LoginRequest;
import ECommerce_pojo.LoginResponse;
import Utilities.PropReadUtil;
import Utilities.SpecsUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PlaceOrder_StepDef {
	
	
	RequestSpecification reqSpecs;
	
	LoginRequest lreq;
	LoginResponse lres;
	RequestSpecification login_G;
	Response login_W;
	String token;
	String userId;
	
	Properties prop; 
	
	@Given("{string} API payload")
	public void api_payload(String _payload) throws FileNotFoundException, IOException {
		
		System.out.println("****GIVEN******"+_payload+"**********");

		//LOGIN TESTCASE
		
		//Base Url and payload set in SpecsUtil , Using object to fetch
		SpecsUtil sutil = new SpecsUtil();		
		reqSpecs = sutil.reqSpecUtil();
				
		//Reading critical details from properties like username and password
		PropReadUtil prUtil = new PropReadUtil();
		prop=prUtil.readProp();
		prop.load(new FileInputStream("src/test/java/resources/config.properties"));
				
		// setting pojo using properties file details
		lreq = new LoginRequest();
		lreq.setUserEmail(prop.getProperty("ecomLogin"));
		lreq.setUserPassword(prop.getProperty("ecomPassword"));
		
		//Given : code starts here
				 login_G = given()
				.log().all()
				.spec(reqSpecs)
				.body(lreq);
	}

	@When("send {string} request with {string}")
	public void send_request_with(String _request, String _resource) {
		
		System.out.println(_request+"******WHEN********"+ _resource);
		
				login_W = login_G.when()
						.post("api/ecom/auth/login");
				
		
		
	}

	@Then("response {string} is equals to {string}")
	public void response_is_equals_to(String _key, String _value) {
		
		System.out.println(_key+"********THEN*********"+_value);
		
		lres = login_W
			.then().extract().response()
			.as(LoginResponse.class);
		
		System.out.println("===================="+lres.getToken()+"==================================");
		token = lres.getToken();
		System.out.println("====================="+lres.getUserId()+"=================================");
		userId=lres.getUserId();
		System.out.println("====================="+lres.getMessage()+"=================================");
		
		assertTrue(lres.getMessage().equals("Login Successfully"));
		
	}


}
