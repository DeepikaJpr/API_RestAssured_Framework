package stepDefinitions;

import static io.restassured.RestAssured.given;

import java.io.File;
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

    @Before(value ="not @Login",order = 0)
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
        RequestSpecification loginSpec = specs.getBaseSpec();

        Response loginResponse = given().spec(loginSpec)
                                        .body(loginRequest)
                                        .post("api/ecom/auth/login");

        LoginResponse lres = loginResponse.as(LoginResponse.class);

        TestContext.setToken(lres.getToken());
        TestContext.setUserId(lres.getUserId());

        System.out.println("HOOK: Login Successful - Token and UserId set.");
    }

    @Before(value = "@DeleteProduct", order = 1)
    public void createProductIfRequired() throws IOException {
        if (TestContext.getProductId() != null) {
            System.out.println("HOOK: Product already created for this thread.");
            return;
        }

        System.out.println("HOOK: Creating product for @DeleteProduct scenario");

        SpecsUtil specs = new SpecsUtil();
        RequestSpecification authSpec = specs.getAuthSpec(TestContext.getToken());

        File file = new File("src/test/java/resources/Screenshot_1.png");

        Response createResponse = given().spec(authSpec)
        		.header("Content-Type","multipart/form-data")
				.multiPart("productImage", file)
                .formParam("productName", "Test123")
                .formParam("productAddedBy", TestContext.getUserId())
                .formParam("productCategory", "fashion")
                .formParam("productSubCategory", "shirts")
                .formParam("productPrice", "9999")
                .formParam("productDescription", "Adidas Originals")
                .formParam("productFor", "women")
                .post("api/ecom/product/add-product");

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
