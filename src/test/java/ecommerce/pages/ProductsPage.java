package ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Products page:
 * https://www.automationexercise.com/products
 */
public class ProductsPage extends AutomationExercisePage {

    /** The sub-url of the Products page. */
    public static final String URL = "products";

    private final static String SEARCH_BAR = "#search_product";
    private final static String SEARCH_BUTTON = "#submit_search";

    private final static String PRODUCT_CARDS = ".productinfo";

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public void searchAndSubmit(String searchItem) {
        WebElement element = driver.findElement(By.cssSelector(SEARCH_BAR));
        element.sendKeys(searchItem);
        WebElement submitButton = driver.findElement(By.cssSelector(SEARCH_BUTTON));
        submitButton.click();
    }

    /**
     * Return a set of all the product names in the product page
     */
    public Set<String> getProductNames() {
        List<WebElement> elements = driver.findElements(By.cssSelector(PRODUCT_CARDS));
        Set<String> productNames =
                elements.stream().map( x -> x.findElement(By.tagName("p")).getText())
                        .collect(Collectors.toSet());
        return productNames;
    }

}
