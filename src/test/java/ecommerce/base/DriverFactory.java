package ecommerce.base;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class DriverFactory {

    private static final DriverFactory INSTANCE = new DriverFactory();

    private static final String DRIVER_DIR = "drivers";

    private static final Logger LOGGER = Logger.getLogger(DriverFactory.class);

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
            if (type == null) {
                LOGGER.info("Using default browser Chrome for creating a web driver");
                threadLocal.set(new ChromeDriver());
            } else {
                switch (type) {
                    case CHROME -> threadLocal.set(new ChromeDriver());
                    case FIREFOX -> threadLocal.set(new FirefoxDriver());
                    case EDGE -> threadLocal.set(new EdgeDriver());
                    default -> {
                    }
                }
            }
        }

        return threadLocal.get();
    }


}
