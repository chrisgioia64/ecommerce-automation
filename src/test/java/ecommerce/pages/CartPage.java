package ecommerce.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.stream.Stream;

/**
 * Page Object for View Cart Page
 * https://www.automationexercise.com/view_cart
 */
public class CartPage extends AutomationExercisePage {

    /** The sub-url of the Cart page. */
    public static final String URL = "view_cart";

    public static final String ROWS_SELECTOR = "#cart_info_table tbody tr";

    public static final String ROW_PRODUCT_NAME_SELECTOR = ".cart_description a";
    public static final String ROW_QUANTITY_SELECTOR = ".cart_quantity button";
    public static final String ROW_UNIT_PRICE_SELECTOR = ".cart_price p";
    public static final String ROW_DELETE_ITEM = ".cart_delete a";

    public static final String CHECKOUT_BUTTON_LINK_TEXT = "Proceed To Checkout";

    public static final String MODAL_POPUP_SELECTOR = "#checkoutModal";
    public static final String LOGIN_LINK_TEXT = "Register / Login";

    private static final Logger LOGGER = Logger.getLogger(CartPage.class);

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void navigateToPage() {
        navigateToSuburl(URL);
    }

    public void clickCheckoutButton() {
        WebElement element = driver.findElement(By.linkText(CHECKOUT_BUTTON_LINK_TEXT));
        element.click();
    }

    /**
     * Clear all items in the cart
     */
    public void clearCart() {
        driver.findElements(By.cssSelector(ROWS_SELECTOR)).stream().forEach(CartPage::deleteRow);
    }

    /**
     * Return a stream of "TR" web elements that contain the product name.
     */
    private Stream<WebElement> getRows(String productName) {
        return driver.findElements(By.cssSelector(ROWS_SELECTOR)).stream()
                .filter(
                        x -> x.findElement(By.cssSelector(ROW_PRODUCT_NAME_SELECTOR)).getText().equalsIgnoreCase(productName));
    }

    /**
     * Return the quantity (or number of units) for the product name
     * If the product name does not exist, return 0
     */
    public int getUnits(String productName) {
        return getRows(productName)
                .map(
                x -> Integer.parseInt(x.findElement(By.cssSelector(ROW_QUANTITY_SELECTOR)).getText()))
                .reduce(0, Math::max);
    }

    /**
     * Return the unit price for the product name
     * If the product name does not exist, returns 0
     */
    public int getUnitPrice(String productName) {
        return
                getRows(productName)
                .map(
                     x -> PageUtils.extractPrice(x.findElement(By.cssSelector(ROW_UNIT_PRICE_SELECTOR)).getText())
                     )
                     .reduce(0, Math::max);
    }

    private static void deleteRow(WebElement rowElement) {
        rowElement.findElement(By.cssSelector(ROW_DELETE_ITEM)).click();
    }

    public void removeFromCart(String productName) {
        getRows(productName).forEach(CartPage::deleteRow);
    }

    public void popupClickOnLoginLink() {
        WebElement element = driver.findElement(By.cssSelector(MODAL_POPUP_SELECTOR));
        WebElement loginLinkElement = element.findElement(By.partialLinkText(LOGIN_LINK_TEXT));
        loginLinkElement.click();
    }


}
