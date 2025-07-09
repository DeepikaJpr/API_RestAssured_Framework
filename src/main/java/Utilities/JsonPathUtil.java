package Utilities;

import io.restassured.path.json.JsonPath;

public class JsonPathUtil {
	
	public Object rawToJson(String stringResponse,String key) {
		
		JsonPath js = new JsonPath(stringResponse);
		return js.get(key);
		
		
	}

}
