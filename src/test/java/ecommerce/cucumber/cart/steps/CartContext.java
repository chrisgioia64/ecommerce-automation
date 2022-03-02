package ecommerce.cucumber.cart.steps;

import ecommerce.base.BrowserType;
import ecommerce.base.DriverFactory;
import ecommerce.base.EnvironmentProperties;
import ecommerce.pages.*;
import org.openqa.selenium.WebDriver;

public class CartContext {

    private final WebDriver driver;
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

        driver = DriverFactory.getInstance().getDriver(type);
        driver.get(url);
        loginPage = new LoginPage(driver);
        productPage = new ProductsPage(driver);
        productDetailsPage = new ProductDetailsPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        paymentPage = new PaymentPage(driver);
        paymentDonePage = new PaymentDonePage(driver);
    }

    public void enterEmailPassword(String email, String password) {
        loginPage.navigateToPage();
        loginPage.enterLogin(email, password);
    }

    public void searchProduct(String productName) {
        productPage.navigateToPage();
        productPage.searchAndSubmit(productName);
    }

    /**
     * Precondition: we are located on the products page
     * @param productName full name of the product
     */
    public boolean viewProductDetails(String productName) {
        return productPage.viewProductDetails(productName);
    }

    /**
     * Precondition: we are located on the product details page
     */
    public void clickAddToCart() {
        productDetailsPage.clickAddToCart();
    }

    public void navigateToCartPage() {
        cartPage.navigateToPage();
    }

    /**
     * Precondition: we are located in the cart page
     */
    public int getQuantity(String productName) {
        return cartPage.getUnits(productName);
    }

    /**
     * Precondition: we are located in the cart page
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

    public void removeFromCart(String productName) {
        cartPage.removeFromCart(productName);
        // this is hacky but we need to give the page time to reload
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setQuantity(int quantity) {
        productDetailsPage.setQuantity(quantity);
    }

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
