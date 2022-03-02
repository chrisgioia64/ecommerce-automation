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
    private final static String PRODUCT_CARDS_OUTER = ".product-image-wrapper";

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void navigateToPage() {
        navigateToSuburl(URL);
    }

    public void searchAndSubmit(String searchItem) {
        sendKeys(SEARCH_BAR, searchItem);
        click(SEARCH_BUTTON);
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

    /**
     * @param productName
     * @return true if the product was found in the page and clicked on, false otherwise
     */
    public boolean viewProductDetails(String productName) {
        List<WebElement> elements = driver.findElements(By.cssSelector(PRODUCT_CARDS_OUTER));
        for (WebElement element : elements) {
            // there are two "p" tags; finds the first "p" tag
            String text = element.findElement(By.tagName("p")).getText();
            if (text.strip().equalsIgnoreCase(productName.strip())) {
                WebElement link = element.findElement(By.cssSelector("a[href*='product_details']"));
                link.click();
                return true;
            }
        }
        return false;
    }

}
