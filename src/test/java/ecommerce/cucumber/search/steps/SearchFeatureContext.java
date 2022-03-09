package ecommerce.cucumber.search.steps;

import ecommerce.base.BaseTest;
import ecommerce.base.BrowserType;
import ecommerce.base.DriverFactory;
import ecommerce.base.EnvironmentProperties;
import ecommerce.pages.HomePage;
import ecommerce.pages.ProductsPage;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

public class SearchFeatureContext {

    private final WebDriver driver;
    private final ProductsPage productPage;

    public SearchFeatureContext() {
        BrowserType type = EnvironmentProperties.getInstance().getBrowserType();
        String url = EnvironmentProperties.getInstance().getUrl();
        driver = BaseTest.getDriver(type);
        driver.get(url);
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

}
