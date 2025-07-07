package Utilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class SpecsUtil {
	
	
	public RequestSpecification reqSpecUtil() throws IOException {
		
		PrintStream pstream = new PrintStream(new FileOutputStream("logging.txt"));
		PropReadUtil prUtil = new PropReadUtil();
		Properties prop = prUtil.readProp();
		
	RequestSpecification reqSpecs = new RequestSpecBuilder()
				.setBaseUri(prop.getProperty("baseURI"))
				.addFilter(RequestLoggingFilter.logRequestTo(pstream))
				.setContentType(ContentType.JSON).build();
	return reqSpecs;
	}
	

}
