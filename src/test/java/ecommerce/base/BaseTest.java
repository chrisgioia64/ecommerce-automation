package ecommerce.base;

import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * The base test class for all TestNg-native tests (not including Cucumber)
 */
public class BaseTest {

    private final static List<WebDriver> driverPool = new ArrayList<>();

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void setupSuite() {
        LOGGER.info(MyMarkers.TEST, "Setup suite");
        RestAssured.baseURI = EnvironmentProperties.getInstance().getUrl();
    }

    public static WebDriver getDriver() {
        BrowserType type = EnvironmentProperties.getInstance().getBrowserType();
        String url = EnvironmentProperties.getInstance().getUrl();
        return getDriver(type, url);
    }

    public static WebDriver getDriver(BrowserType browserType) {
        String url = EnvironmentProperties.getInstance().getUrl();
        return getDriver(browserType, url);
    }

    public static WebDriver getDriver(BrowserType browserType, String url) {
        WebDriver driver = DriverFactory.getInstance().getDriver(browserType);
        driver.get(url);
        driverPool.add(driver);
        return driver;
    }

    @AfterSuite
    public void tearDownSuite() {
        LOGGER.info(MyMarkers.TEST, "Beginning tear down of TestNG suite");
        LOGGER.info(MyMarkers.TEST, "There are {} webdrivers to close", driverPool.size());
        driverPool.forEach(WebDriver::quit);
        LOGGER.info(MyMarkers.TEST, "Finishing tear down of TestNG suite");
    }

}
