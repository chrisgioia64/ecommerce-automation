package ecommerce.pages;

import ecommerce.base.MyMarkers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
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

    private final static String BRANDS_DIV = ".brands-name";
    private final static String BRANDS_LIST_ITEM = "li";
    private final static String BRANDS_COUNT_SPAN = "span";




    private final static Logger LOGGER = LogManager.getLogger(ProductsPage.class);

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
     * A data type for brand information that is used by a data provider
     */
    public static class BrandInfo {
        /** The text description of the brand (e.g. "Polo") */
        public String brandName;
        /** The full url of the page that list all products for the brand. */
        public String brandUrl;
        /** The number of products that contains this brand. */
        public int count;

        public BrandInfo(String brandName, String brandUrl, int count) {
            this.brandName = brandName;
            this.brandUrl = brandUrl;
            this.count = count;
        }
    }

    /**
     * Returns the brand information for each brand listed on the left sidebar
     */
    public List<BrandInfo> getBrandInfo() {
        WebElement element = driver.findElement(By.cssSelector(BRANDS_DIV));
        List<WebElement> items = element.findElements(By.cssSelector(BRANDS_LIST_ITEM));
        List<BrandInfo> result = new LinkedList<>();
        for (WebElement item : items) {
            WebElement linkElement = item.findElement(By.cssSelector("a"));
            String brandName = parseBrandName(linkElement.getText());
            String link = linkElement.getAttribute("href");
            WebElement spanElement = item.findElement(By.cssSelector(BRANDS_COUNT_SPAN));
            int count = parseBrandCount(spanElement.getText());
            BrandInfo info = new BrandInfo(brandName, link ,count);
            result.add(info);
        }
        return result;
    }

    private int parseBrandCount(String text) {
        try {
            return Integer.parseInt(text.substring(1, text.length() - 1));
        } catch (NumberFormatException ex) {
            LOGGER.info(MyMarkers.PAGE_OBJECTS,
                    "Could not parse the brand count from {}", text);
            return -1;
        }
    }

    /**
     * Parse the brand name from the link text (e.g. " (6)\nPolo")
     */
    private String parseBrandName(String text) {
        char[] arr = text.toCharArray();
        int index = 0;
        while (index < arr.length) {
            if (arr[index] == ')') {
                // there's a newline character we need to ignore
                return text.substring(index+2);
            }
            index++;
        }
        return "";
    }

    /**
     * Return a set of all the product names in the product page
     */
    public Set<String> getProductNames() {
        List<WebElement> elements = driver.findElements(By.cssSelector(PRODUCT_CARDS));
        return elements.stream().map( x -> x.findElement(By.tagName("p")).getText())
                        .collect(Collectors.toSet());
    }

    /**
     * Click on the product with PRODUCTNAME if it exists
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
