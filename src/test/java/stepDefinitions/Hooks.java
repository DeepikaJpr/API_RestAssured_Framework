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
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Hooks {

    @Before(value = "not @Login", order = 0)
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
    public void createProductBeforeScenario() throws IOException {
        if (TestContext.getProductId() != null) {
            System.out.println("HOOK: Product already created for this thread.");
            return;
        }

        System.out.println("HOOK: Creating product for @CreateProduct scenario");

        SpecsUtil specs = new SpecsUtil();
        RequestSpecification authSpec = specs.getAuthSpec(TestContext.getToken());

        File file = new File("src/test/resources/Screenshot_1.png");

        Response createResponse = given().spec(authSpec)
                .header("Content-Type", "multipart/form-data")
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

    @After(value = "@CreateProduct", order = 1)
    public void deleteProductAfterScenario() throws IOException {
        if (TestContext.getProductId() == null) {
            System.out.println("AFTER HOOK: No product to delete.");
            return;
        }

        System.out.println("AFTER HOOK: Deleting product with ID: " + TestContext.getProductId());

        SpecsUtil specs = new SpecsUtil();
        RequestSpecification authSpec = specs.getAuthSpec(TestContext.getToken());

        Response deleteResponse = given().spec(authSpec)
                .pathParam("productId", TestContext.getProductId())
                .delete("api/ecom/product/delete-product/{productId}");

        String response = deleteResponse.asString();
        JsonPathUtil js = new JsonPathUtil();
        String message = (String) js.rawToJson(response, "message");

        System.out.println("AFTER HOOK: Delete Response Message: " + message);
    }

    @After(order = 0)
    public void resetContext(Scenario scenario) {
        System.out.println("AFTER: Resetting TestContext for scenario: " + scenario.getName());
        TestContext.reset();
    }
}
