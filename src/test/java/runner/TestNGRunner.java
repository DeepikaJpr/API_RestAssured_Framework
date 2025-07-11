/*
 * package runner;
 * 
 * import org.junit.runner.RunWith;
 * 
 * import io.cucumber.junit.Cucumber; import io.cucumber.junit.CucumberOptions;
 * 
 * @RunWith(Cucumber.class)
 * 
 * @CucumberOptions( features = "src/test/java/Features", glue =
 * "stepDefinitions", plugin = {"pretty", "summary"} // ,tags="@DeleteProduct"
 * 
 * ) public class TestRunner { }
 */


package runner;

import org.testng.annotations.DataProvider;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/java/Features",
    glue = "stepDefinitions",
    plugin = {"pretty", "summary"}
    //tags = "@Regression"
)
public class TestNGRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
