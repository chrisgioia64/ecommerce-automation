package ecommerce.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


public class DriverFactory {

    private static final DriverFactory INSTANCE = new DriverFactory();

    /** The folder that contains the webdrivers such as chromedriver.exe. */
    private static final String DRIVER_DIR = "drivers";

    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
        this.useRemoteWebDriver = EnvironmentProperties.getInstance().isSauceLabs();
        this.isMultipleDrivers = EnvironmentProperties.getInstance().isMultipleDrivers();
        initializaLocalProperties();
        initializeSauceProperties();
    }

    private Map<BrowserType, MutableCapabilities> capabilityMap;
    private String sauceUrl;
    private boolean useRemoteWebDriver;
    private boolean isMultipleDrivers;

    private void initializeSauceProperties() {
        this.capabilityMap = new HashMap<>();
        AbstractDriverOptions chromeCapabilities = createCapabilities(BrowserType.CHROME);
        capabilityMap.put(BrowserType.CHROME, chromeCapabilities);

        AbstractDriverOptions firefoxCapabilities = createCapabilities(BrowserType.FIREFOX);
        capabilityMap.put(BrowserType.FIREFOX, firefoxCapabilities);

        AbstractDriverOptions edgeCapabilities = createCapabilities(BrowserType.EDGE);
        capabilityMap.put(BrowserType.EDGE, edgeCapabilities);
        String sauceUrl = String.format(" https://ondemand.us-west-1.saucelabs.com/wd/hub");
        this.sauceUrl = sauceUrl;
    }

    private AbstractDriverOptions createOptions(BrowserType type) {
        return switch (type) {
            case CHROME -> new ChromeOptions();
            case FIREFOX -> new FirefoxOptions();
            case EDGE -> new EdgeOptions();
            default -> {
                throw new IllegalArgumentException("browser type specified is unrecognized: " + type);
            }
        };
    }

    private AbstractDriverOptions createCapabilities(BrowserType type) {
        AbstractDriverOptions sauceOptions = createOptions(type);
        String sauceUser = EnvironmentProperties.getInstance().getProperty(EnvironmentProperties.SAUCE_USERNAME);
        String sauceKey = EnvironmentProperties.getInstance().getProperty(EnvironmentProperties.SAUCE_ACCESS_KEY);
        sauceOptions.setCapability("username", sauceUser);
        sauceOptions.setCapability("accessKey", sauceKey);
        AbstractDriverOptions capabilities = createOptions(type);
        capabilities.setCapability("platformName", "Windows 10");
        capabilities.setCapability("sauce:options", sauceOptions);
        capabilities.setBrowserVersion("latest");
        return capabilities;
    }

    private WebDriver createWebdriver(BrowserType type) {
        WebDriver driver = createWebdriverHelper(type);
        if (driver != null) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        }
        return driver;
    }

    private WebDriver createWebdriverHelper(BrowserType type) {
        if (this.useRemoteWebDriver) {
            try {
                return createRemoteWebdriver(type);
            } catch (MalformedURLException e) {
                LOGGER.warn(MyMarkers.DRIVER,
                        "There was a problem with the url of the remote driver " + e.getMessage());
                return null;
            }
        } else {
            return createLocalWebdriver(type);
        }
    }

    private WebDriver createLocalWebdriver(BrowserType type) {
        return switch (type) {
            case FIREFOX -> new FirefoxDriver();
            case CHROME -> new ChromeDriver();
            case EDGE -> new EdgeDriver();
            default -> {
                throw new IllegalArgumentException("browser type specified is unrecognized: " + type);
            }
        };
    }

    private WebDriver createRemoteWebdriver(BrowserType type) throws MalformedURLException {
        MutableCapabilities capabilities = this.capabilityMap.get(type);
        return new RemoteWebDriver(new URL(sauceUrl), capabilities);
//        return new RemoteWebDriver(new URL(sauceUrl), new ChromeOptions());
    }

    private void initializaLocalProperties() {
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

    private final static BrowserMap singleThreadMap = new BrowserMap();

    /**
     * Each thread gets at most one web driver.
     */
    public synchronized WebDriver getDriver(BrowserType type) {
        if (isMultipleDrivers) {
            if (threadLocal.get() == null) {
                BrowserMap map = new BrowserMap();
                threadLocal.set(map);
                if (type == null) {
                    LOGGER.info(MyMarkers.DRIVER,
                            "For thread id {}, using default browser Chrome for creating a web driver",
                            Thread.currentThread().getName());
                    map.map.put(BrowserType.CHROME, createWebdriver(BrowserType.CHROME));
                } else {
                    LOGGER.info(MyMarkers.DRIVER,
                            "thread id {} does not contain the browser type {}",
                            Thread.currentThread().getName(), type.toString());
                    map.map.put(type, createWebdriver(type));
                }
            } else {
                BrowserMap map = threadLocal.get();
                if (!map.map.containsKey(type)) {
                    LOGGER.info(MyMarkers.DRIVER,
                            "thread id {} does not contain the browser type {}",
                            Thread.currentThread().getName(), type.toString());
                    map.map.put(type, createWebdriver(type));
                }
            }
            return threadLocal.get().map.get(type);
        } else {
                WebDriver driver = DriverFactory.singleThreadMap.map.get(type);
                if (driver == null) {
                    driver = createWebdriver(type);
                    singleThreadMap.map.put(type, driver);
                }
                return driver;

        }
    }


}
