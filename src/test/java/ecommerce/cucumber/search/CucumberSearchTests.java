package ecommerce.cucumber.search;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/ecommerce/cucumber/search/features",
        glue = "ecommerce.cucumber.search.steps"
)
public class CucumberSearchTests extends AbstractTestNGCucumberTests {

}
