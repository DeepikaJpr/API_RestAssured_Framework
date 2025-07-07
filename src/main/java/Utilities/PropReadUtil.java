package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropReadUtil {
	
	public Properties readProp() throws FileNotFoundException, IOException {
		
		Properties prop = new Properties();
		prop.load(new FileInputStream("src/test/java/resources/config.properties"));
		
		return prop;
	}

}
