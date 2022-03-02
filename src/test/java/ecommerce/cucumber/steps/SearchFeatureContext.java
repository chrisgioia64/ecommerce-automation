package ecommerce.cucumber.steps;

import ecommerce.base.BrowserType;
import ecommerce.base.DriverFactory;
import ecommerce.base.EnvironmentProperties;
import ecommerce.pages.HomePage;
import ecommerce.pages.ProductsPage;
import org.openqa.selenium.WebDriver;

public class SearchFeatureContext {

    private WebDriver driver;
    private HomePage homePage;
    private ProductsPage productPage;

    public SearchFeatureContext() {
        BrowserType type = EnvironmentProperties.getInstance().getBrowserType();
        String url = EnvironmentProperties.getInstance().getUrl();
        driver = DriverFactory.getInstance().getDriver(type);
        driver.get(url);
        homePage = new HomePage(driver);
        productPage = new ProductsPage(driver);
    }

    public void navigateToProductPage() {
        driver.get(EnvironmentProperties.getInstance().getUrl() + "/" + ProductsPage.URL);
    }

    public void searchFor(String search) {
        productPage.searchAndSubmit(search);
    }

    public boolean containsProductName(String productName) {
        return productPage.getProductNames().contains(productName);
    }

    public void close() {
        // right now, we share the driver across different search contexts
        // so we cannot terminate the driver
//        driver.quit();
    }

}
