package ecommerce.cucumber.cart;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/ecommerce/cucumber/cart/features",
        glue = "ecommerce.cucumber.cart.steps"
)
public class CucumberCartTests extends AbstractTestNGCucumberTests  {

}
