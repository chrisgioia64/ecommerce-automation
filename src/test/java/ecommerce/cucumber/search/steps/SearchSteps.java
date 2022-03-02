package ecommerce.cucumber.search.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;


public class SearchSteps {

    private SearchFeatureContext context;

    @Before
    public void setup() {
        context = new SearchFeatureContext();
    }

    @After
    public void teardown() {
        context.close();
    }

    @Given("I am on the products page")
    public void i_am_on_the_products_page() {
        context.navigateToProductPage();
    }

    @When("Search for {string}")
    public void search_for(String string) {
        context.searchFor(string);
    }

    @Then("Product Search contains product {string}")
    public void product_search_contains_product(String string) {
        assertThat(true, Matchers.equalTo(context.containsProductName(string)));
    }

    @Then("Product Search does not contain product {string}")
    public void product_search_does_not_contain_product(String string) {
        assertThat(false, Matchers.equalTo(context.containsProductName(string)));
    }

}
