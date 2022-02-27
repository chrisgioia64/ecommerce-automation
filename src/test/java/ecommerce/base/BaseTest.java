package ecommerce.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    private final static List<WebDriver> driverPool = new ArrayList<>();

    @BeforeSuite
    public void setupSuite() {
    }

    public WebDriver getDriver() {
        BrowserType type = EnvironmentProperties.getInstance().getBrowserType();
        String url = EnvironmentProperties.getInstance().getUrl();
        return getDriver(type, url);
    }

    public WebDriver getDriver(BrowserType browserType, String url) {
        WebDriver driver = DriverFactory.getInstance().getDriver(browserType);
        driver.get(url);
        driverPool.add(driver);
        return driver;
    }

    @AfterSuite
    public void tearDownSuite() {
        driverPool.forEach(WebDriver::quit);
    }

}
