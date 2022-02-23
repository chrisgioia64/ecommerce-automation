package ecommerce.automation.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {

    private static final DriverFactory INSTANCE = new DriverFactory();

    private static final String DRIVER_DIR = "drivers";

    private DriverFactory() {
        initializeProperties();
    }

    private void initializeProperties() {
        System.setProperty("webdriver.edge.driver", getDriverLocation("msedgedriver.exe"));
        System.setProperty("webdriver.chrome.driver",getDriverLocation("chromedriver.exe"));
        System.setProperty("webdriver.gecko.driver", getDriverLocation("geckodriver.exe"));
        System.setProperty("webdriver.ie.driver", getDriverLocation("IEDriverServer.exe"));
    }

    private static String getDriverLocation(String baseName) {
        return DRIVER_DIR + "/" + baseName;
    }

    public static DriverFactory getInstance() {
        return INSTANCE;
    }

    private final static ThreadLocal<WebDriver> threadLocal = new ThreadLocal<>() {
    };

    public WebDriver getDriver(BrowserType type) {
        if (threadLocal.get() == null) {
            switch (type) {
                case CHROME -> threadLocal.set(new ChromeDriver());
                case FIREFOX -> threadLocal.set(new FirefoxDriver());
                case EDGE -> threadLocal.set(new EdgeDriver());
                default -> {
                }
            }
        }

        return threadLocal.get();
    }

    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE,
        IE
    }

}
