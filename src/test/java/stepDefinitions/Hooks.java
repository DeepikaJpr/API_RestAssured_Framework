package stepDefinitions;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.Properties;

import ECommerce_pojo.LoginRequest;
import ECommerce_pojo.LoginResponse;
import Utilities.PropReadUtil;
import Utilities.SpecsUtil;
import Utilities.TestContext;
import Utilities.JsonPathUtil;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Hooks {

    // Runs before every scenario to ensure login is performed
    
	@Before(order = 0)
    public void ensureLogin() throws IOException {
        if (TestContext.getToken() != null && TestContext.getUserId() != null) {
            System.out.println("HOOK: Login already done. Skipping...");
            return;
        }

        System.out.println("HOOK: Performing login...");

        PropReadUtil prUtil = new PropReadUtil();
        Properties prop = prUtil.readProp();

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserEmail(prop.getProperty("ecomLogin"));
        loginRequest.setUserPassword(prop.getProperty("ecomPassword"));

        SpecsUtil specs = new SpecsUtil();
        RequestSpecification loginSpec = specs.getLoginReqSpec();

        Response loginResponse = given().spec(loginSpec).body(loginRequest).post("api/ecom/auth/login");
        LoginResponse lres = loginResponse.as(LoginResponse.class);

        TestContext.setToken(lres.getToken());
        TestContext.setUserId(lres.getUserId());

        System.out.println("HOOK: Login Successful - Token and UserId set.");
    }

    // Runs only before scenarios with @DeleteProduct to create a product
    @Before(value="@DeleteProduct",order = 1)
    public void createProductIfRequired() throws IOException {
        if (TestContext.getProductId() != null) {
            System.out.println("HOOK: Product already created for this thread.");
            return;
        }

        System.out.println("HOOK: Creating product for @DeleteProduct scenario");

        SpecsUtil specs = new SpecsUtil();
        RequestSpecification createProductSpec = specs.getCreateProductReqSpec(
                TestContext.getToken(),
                TestContext.getUserId(),
                specs.getLoginReqSpec()
        );

        Response createResponse = given().spec(createProductSpec).post("api/ecom/product/add-product");
        String responseBody = createResponse.asString();

        JsonPathUtil js = new JsonPathUtil();
        String productId = (String) js.rawToJson(responseBody, "productId");

        TestContext.setProductId(productId);
        System.out.println("HOOK: Product created with ID = " + productId);
    }

    @After
    public void cleanUp() {
        System.out.println("AFTER: Resetting TestContext");
        TestContext.reset();
    }
}
