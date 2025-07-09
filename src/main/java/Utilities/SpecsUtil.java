package Utilities;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class SpecsUtil {
	
	PrintStream pstream;
	PropReadUtil prUtil ;
	Properties prop; 
	RequestSpecification login_reqSpecs ;
	RequestSpecification createProduct_reqSpecs;
	
	public RequestSpecification getLoginReqSpec() throws IOException {
		
		pstream = new PrintStream(new FileOutputStream("logging.txt",true));
		prUtil = new PropReadUtil();
		prop = prUtil.readProp();
		
	login_reqSpecs = new RequestSpecBuilder()
				.setBaseUri(prop.getProperty("baseURI"))
				.addFilter(RequestLoggingFilter.logRequestTo(pstream))
				.setContentType(ContentType.JSON).build();
	return login_reqSpecs;
	}
	
	public RequestSpecification getCreateProductReqSpec(String token,String userId,RequestSpecification base_reqspecs)
	{
		File file = new File("src/test/java/resources/Screenshot_1.png"); // works locally
		System.out.println("====FILE Exists=========="+file.exists()+"==========");
		
		createProduct_reqSpecs = new RequestSpecBuilder()
									.addRequestSpecification(base_reqspecs)
									.addHeader("Content-Type","multipart/form-data")
									.addHeader("Authorization", token)
									.addParam("productName", "Test123")
									.addParam("productAddedBy", userId)
									.addParam("productCategory", "fashion")
									.addParam("productSubCategory", "shirts")
									.addParam("productPrice", "9999")
									.addParam("productDescription", "Addias Originals")
									.addParam("productFor","women")
									.addMultiPart("productImage", file)
									.build();
		return createProduct_reqSpecs;
									
	}
	
	public RequestSpecification getDeleteProductReqSpec(String token,RequestSpecification base_reqspecs) {
		return new RequestSpecBuilder()
				.addRequestSpecification(base_reqspecs)
				.addHeader("Authorization", token)
				.setContentType(ContentType.JSON)
				.build();
				
	}
	

}
