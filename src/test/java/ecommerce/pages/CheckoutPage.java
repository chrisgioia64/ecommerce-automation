package ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object for Checkout Page
 * https://www.automationexercise.com/checkout
 */
public class CheckoutPage extends AutomationExercisePage {

    public static final String URL = "checkout";

    public static final String ROWS_SELECTOR = "#cart_info tbody tr";
    public static final String PRICE_TEXT_SELECTOR = "p";

    public static final String PLACE_ORDER_LINK_TEXT = "Place Order";

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void navigateToPage() {
        navigateToSuburl(URL);
    }

    /**
     * Return the total price (in rupees, of all products)
     */
    public int getTotalPrice() {
        List<WebElement> webElements = driver.findElements(By.cssSelector(ROWS_SELECTOR));
        WebElement lastRow = webElements.get(webElements.size()-1);
        String priceText = lastRow.findElement(By.cssSelector(PRICE_TEXT_SELECTOR)).getText();
        return PageUtils.extractPrice(priceText);
    }

    public void placeOrder() {
        WebElement element = driver.findElement(By.linkText(PLACE_ORDER_LINK_TEXT));
        element.click();
    }

}
