package ecommerce.cucumber.cart.steps;

import ecommerce.base.EnvironmentProperties;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.asserts.SoftAssert;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.fail;

/**
 * Separated out the logic between three classes:
 * - Steps: contains the Cucumber steps, hooks, and assertions when possible
 * - Context: contains a wrapper around the driver and page objects
 * - Page Objects: This is a common design pattern for Selenium tests
 */
public class CartSteps {

    private CartContext context;
    private double stepWait;

    @Before
    public void setup() {
        stepWait = EnvironmentProperties.getInstance().getCucumberWait();
        context = new CartContext();
    }

    @After
    public void after() {
        context.clearCart();
        context.logout();
    }

    @BeforeStep
    public void beforeStep() {
        sleepMs((int)(stepWait * 1000));
//        context.implicitWait(stepWait);
    }

    private void sleepMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Given("I login with email {string} and password {string}")
    public void i_login_with_email_and_password(String email, String password) {
        context.enterEmailPassword(email, password);
    }

    @Given("Search for {string} on the products page")
    public void search_for_on_the_products_page(String productName) {
        context.searchProduct(productName);
    }

    @Given("View Product Details for {string}")
    public void view_product_details_for(String productName) {
        boolean found = context.viewProductDetails(productName);
        if (!found) {
            fail("could not find product details for " + productName);
        }
    }

    @Given("Add to Cart")
    public void add_to_cart() {
        sleepMs(500);
        context.clickAddToCart();
        sleepMs(500);
    }

    @Given("I navigate to the cart page")
    public void i_navigate_to_the_cart_page() {
        context.navigateToCartPage();
    }

    @Given("Verify {int} {string} at Rs. {int} each")
    public void verify_at_rs_each(Integer expectedQuantity,
                                  String productName, Integer expectedPrice) {
        int actualQuantity = context.getQuantity(productName);
        int actualPrice = context.getUnitPrice(productName);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(
                actualQuantity, expectedQuantity, 1e-1, "For product name " + productName + " " +
                        "expected quantity and actual quantity do not match");
        softAssert.assertEquals(
                actualPrice, expectedPrice, 1e-1, "For product name " + productName + " " +
                        "expected unit price and actual unit price do not match"
        );
        softAssert.assertAll();
    }

    @Given("Verify {int} {string}")
    public void verify_at_rs_each(Integer expectedQuantity, String productName) {
        int actualQuantity = context.getQuantity(productName);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(
                actualQuantity, expectedQuantity, 1e-1, "For product name " + productName + " " +
                        "expected quantity and actual quantity do not match");
        softAssert.assertAll();
    }

    @Given("Proceed to Checkout")
    public void proceed_to_checkout() {
        context.clickCheckoutButton();
    }

    @Given("^Verify total amount is Rs. (\\d+)$")
    public void verify_total_amount_is_rs(Integer expectedPrice) {
        int actualPrice = context.getTotalPrice();
        assertEquals(actualPrice, expectedPrice, 1e-1,
                "Total price for checkout does not match");
    }


    @Given("Place Order")
    public void place_order() {
        context.placeOrder();
    }

    @Given("Enter in dummy credit card")
    public void enter_in_dummy_credit_card() {
        context.enterDummyCardInformation();
    }

    @Then("Verify Order has been placed")
    public void order_has_been_placed() {
        assertTrue(context.onPaymentDonePage(), "We did not reach the payment done page");
    }

    @Given("Remove from cart {string}")
    public void remove_from_cart(String productName) {
        context.removeFromCart(productName);
    }

    @Given("Verify No {string}")
    public void verify_no(String productName) {
        int actualQuantity = context.getQuantity(productName);
        int expectedQuantity = 0;
        assertEquals(actualQuantity, expectedQuantity, 1e-1,
                "In cart expected no items of product name " + productName);
    }

    @Given("Set quantity to {int}")
    public void set_quantity_to(Integer newQuantity) {
        context.setQuantity(newQuantity);
    }

    @Given("Click on login popup")
    public void click_on_login_popup() {
        context.popupClickOnLoginLink();
    }


}
