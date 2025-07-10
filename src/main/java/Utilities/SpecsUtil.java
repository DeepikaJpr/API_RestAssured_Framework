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

    private static PrintStream pstream;
    private static Properties prop;

    public SpecsUtil() throws IOException {
        if (prop == null) {
            prop = new PropReadUtil().readProp();
        }
        if (pstream == null) {
            pstream = new PrintStream(new FileOutputStream("logging.txt", true));
        }
    }

    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(prop.getProperty("baseURI"))
                .addFilter(RequestLoggingFilter.logRequestTo(pstream))
                .setContentType(ContentType.JSON)
                .build();
    }

    public RequestSpecification getAuthSpec(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(prop.getProperty("baseURI"))
                .addFilter(RequestLoggingFilter.logRequestTo(pstream))
                .addHeader("Authorization", token)
                .setContentType(ContentType.JSON)
                .build();
    }
}
