package ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Product details page
 * e.g. https://www.automationexercise.com/product_details/1
 */
public class ProductDetailsPage extends AutomationExercisePage {

    public static final String PAGE_URL = "product_details";

    private static final String PRODUCT_NAME_H2 = ".product-information h2";
    private static final String PRODUCT_CATEGORY_TEXT = ".product-information p";
    private static final String PRODUCT_PRICE_TEXT = ".product-information span span";
    private static final String PRODUCT_BRAND_TEXT = ".product-information p:nth-of-type(4)";

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        WebElement element = driver.findElement(By.cssSelector(PRODUCT_NAME_H2));
        return element.getText();
    }

    public String getCategoryText() {
        WebElement element = driver.findElement(By.cssSelector(PRODUCT_CATEGORY_TEXT));
        return element.getText();
    }

    public String parseOuterCategory() {
        String categoryText = getCategoryText();
        String[] arr = categoryText.split(":")[1].split(">");
        if (arr.length == 2) {
            return arr[0].strip();
        } else {
            return "";
        }
    }

    public String parseInnerCategory() {
        String categoryText = getCategoryText();
        String[] arr = categoryText.split(":")[1].split(">");
        if (arr.length == 2) {
            return arr[1].strip();
        } else {
            return "";
        }
    }


    public String getPriceText() {
        WebElement element = driver.findElement(By.cssSelector(PRODUCT_PRICE_TEXT));
        return element.getText();
    }

    public Integer parsePrice() {
        String priceTest = getPriceText();
        String[] arr = priceTest.split("\\s+");
        if (arr.length == 2) {
            try {
                return Integer.parseInt(arr[1]);
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public String getBrandText() {
        WebElement element = driver.findElement(By.cssSelector(PRODUCT_BRAND_TEXT));
        return element.getText();
    }

    public String parseBrand() {
        String brandText = getBrandText();
        String[] arr = brandText.split(":");
        if (arr.length == 2) {
            return arr[1].strip();
        } else {
            return "";
        }
    }

}
