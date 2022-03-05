package ecommerce.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.HashMap;
import java.util.Map;


public class DriverFactory {

    private static final DriverFactory INSTANCE = new DriverFactory();

    /** The folder that contains the webdrivers such as chromedriver.exe. */
    private static final String DRIVER_DIR = "drivers";

    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);

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

    /**
     * A map of all the browsers for a given thread
     */
    private static class BrowserMap {
        private final Map<BrowserType, WebDriver> map;

        public BrowserMap() {
            map = new HashMap<>();
        }
    }

    private final static ThreadLocal<BrowserMap> threadLocal = new ThreadLocal<>() {
    };

    /**
     * Each thread gets at most one web driver.
     */
    public WebDriver getDriver(BrowserType type) {
        if (threadLocal.get() == null) {
            BrowserMap map = new BrowserMap();
            threadLocal.set(map);
            if (type == null) {
                LOGGER.info(MyMarkers.DRIVER,
                        "For thread id {}, using default browser Chrome for creating a web driver",
                        Thread.currentThread().getId());
                map.map.put(BrowserType.CHROME, new ChromeDriver());
            } else {
                LOGGER.info(MyMarkers.DRIVER,
                        "thread id {} does not contain the browser type {}",
                        Thread.currentThread().getId(), type.toString());
                switch (type) {
                    case CHROME -> map.map.put(BrowserType.CHROME, new ChromeDriver());
                    case FIREFOX -> map.map.put(BrowserType.FIREFOX, new FirefoxDriver());
                    case EDGE -> map.map.put(BrowserType.EDGE, new EdgeDriver());
                    default -> {
                    }
                }
            }
        } else {
            BrowserMap map = threadLocal.get();
            if (!map.map.containsKey(type)) {
                LOGGER.info(MyMarkers.DRIVER,
                        "thread id {} does not contain the browser type {}",
                        Thread.currentThread().getId(), type.toString());
                switch (type) {
                    case CHROME -> map.map.put(BrowserType.CHROME, new ChromeDriver());
                    case FIREFOX -> map.map.put(BrowserType.FIREFOX, new FirefoxDriver());
                    case EDGE -> map.map.put(BrowserType.EDGE, new EdgeDriver());
                    default -> {
                    }
                }
            }
        }
        return threadLocal.get().map.get(type);
    }


}
