package ecommerce.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/ecommerce/cucumber/features",
        glue = "ecommerce.cucumber.steps"
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {

}
