package ecommerce.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BrandProductsPage extends ProductsPage {

    private final static String BRAND_BREADCRUMB = ".breadcrumbs .breadcrumb li.active";

    public final static String PAGE_URL = "brand_products/";

    public BrandProductsPage(WebDriver driver) {
        super(driver);
    }

    public String getBrandBreadcrumb() {
        WebElement element = driver.findElement(By.cssSelector(BRAND_BREADCRUMB));
        return element.getText();
    }

}
