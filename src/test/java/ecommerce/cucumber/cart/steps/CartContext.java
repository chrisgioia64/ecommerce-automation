package ecommerce.cucumber.cart.steps;

import ecommerce.base.BrowserType;
import ecommerce.base.DriverFactory;
import ecommerce.base.EnvironmentProperties;
import ecommerce.pages.*;
import org.openqa.selenium.WebDriver;

public class CartContext {

    private final LoginPage loginPage;
    private final ProductsPage productPage;
    private final ProductDetailsPage productDetailsPage;
    private final CartPage cartPage;
    private final CheckoutPage checkoutPage;
    private final PaymentPage paymentPage;
    private final PaymentDonePage paymentDonePage;

    public CartContext() {
        BrowserType type = EnvironmentProperties.getInstance().getBrowserType();
        String url = EnvironmentProperties.getInstance().getUrl();

        WebDriver driver = DriverFactory.getInstance().getDriver(type);
        driver.get(url);
        loginPage = new LoginPage(driver);
        productPage = new ProductsPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        paymentPage = new PaymentPage(driver);
        paymentDonePage = new PaymentDonePage(driver);
    }

    /**
     * Navigate to the login page and enter in credentials
     */
    public void enterEmailPassword(String email, String password) {
        loginPage.navigateToPage();
        loginPage.enterLogin(email, password);
    }

    /**
     * Navigate to product page and use search bar to search for PRODUCTNAME
     */
    public void searchProduct(String productName) {
        productPage.navigateToPage();
        productPage.searchAndSubmit(productName);
    }

    /**
     * Given we are on the products page, view the product details
     * for the product with name PRODUCTNAME (if it exists)
     */
    public boolean viewProductDetails(String productName) {
        return productPage.viewProductDetails(productName);
    }

    /**
     * Given we are on the product details page, add the product to the cart
     */
    public void clickAddToCart() {
        productDetailsPage.clickAddToCart();
    }

    public void navigateToCartPage() {
        cartPage.navigateToPage();
    }

    /**
     * Return the quantity (number of units) of PRODUCTNAME in the cart page
     */
    public int getQuantity(String productName) {
        return cartPage.getUnits(productName);
    }

    /**
     * Return the unit price of PRODUCTNAME in the cart page
     */
    public int getUnitPrice(String productName) {
       return cartPage.getUnitPrice(productName);
    }

    public void clickCheckoutButton() {
        cartPage.clickCheckoutButton();
    }

    public int getTotalPrice() {
        return checkoutPage.getTotalPrice();
    }

    public void placeOrder() {
        checkoutPage.placeOrder();
    }

    public void enterDummyCardInformation() {
        paymentPage.enterCardInformation("Tom Sawyer", "20206583",
                "311", "05", "1985");
    }

    /**
     * Returns true if we contain the "Download Invoice" link that
     * indicates we are on the Payment Done page
     */
    public boolean onPaymentDonePage() {
        return paymentDonePage.downloadInvoiceButtonExists();
    }

    /**
     * Removes the product PRODUCTNAME from the cart (if it exists)
     * Since this procedure might take some time to update the front-end,
     * add an artificial wait.
     */
    public void removeFromCart(String productName) {
        cartPage.removeFromCart(productName);
        // this is hacky but we need to give the page time to reload
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * On the product details page, set the quantity of the selected product
     */
    public void setQuantity(int quantity) {
        productDetailsPage.setQuantity(quantity);
    }

    /**
     * When there is nobody logged in, and we are on the cart page and attempt
     * to checkout, a popup will appear asking us if we want to sign in.
     * Click on the "login" link of this popup.
     */
    public void popupClickOnLoginLink() {
        cartPage.popupClickOnLoginLink();
    }

    /**
     * If we are logged in, logs us out.
     */
    public void logout() {
        productPage.clickLogoutLinkIfExists();
    }

}
