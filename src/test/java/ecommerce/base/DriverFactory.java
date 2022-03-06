package ecommerce.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class DriverFactory {

    private static final DriverFactory INSTANCE = new DriverFactory();

    /** The folder that contains the webdrivers such as chromedriver.exe. */
    private static final String DRIVER_DIR = "drivers";

    private static final Logger LOGGER = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
        this.useRemoteWebDriver = EnvironmentProperties.getInstance().isSauceLabs();
        initializaLocalProperties();
        initializeSauceProperties();
    }

    private Map<BrowserType, MutableCapabilities> capabilityMap;
    private String sauceUrl;
    private boolean useRemoteWebDriver;

    private void initializeSauceProperties() {
        this.capabilityMap = new HashMap<>();
        MutableCapabilities chromeCapabilities = createCapabilities();
        chromeCapabilities.setCapability("browserName", "chrome");
        chromeCapabilities.setCapability("browserVersion", "98.0");
        capabilityMap.put(BrowserType.CHROME, chromeCapabilities);
        MutableCapabilities firefoxCapabilities = createCapabilities();
        firefoxCapabilities.setCapability("browserName", "firefox");
        firefoxCapabilities.setCapability("browserVersion", "96.0");
        capabilityMap.put(BrowserType.FIREFOX, firefoxCapabilities);
        MutableCapabilities edgeCapabilities = createCapabilities();
        edgeCapabilities.setCapability("browserName", "edge");
        edgeCapabilities.setCapability("browserVersion", "99.0");
        capabilityMap.put(BrowserType.EDGE, edgeCapabilities);
        String sauceUrl = String.format(" https://ondemand.us-west-1.saucelabs.com/wd/hub");
        this.sauceUrl = sauceUrl;
    }

    private MutableCapabilities createCapabilities() {
        MutableCapabilities sauceOptions = new MutableCapabilities();
        String sauceUser = EnvironmentProperties.getInstance().getProperty(EnvironmentProperties.SAUCE_USERNAME);
        String sauceKey = EnvironmentProperties.getInstance().getProperty(EnvironmentProperties.SAUCE_ACCESS_KEY);
        sauceOptions.setCapability("username", sauceUser);
        sauceOptions.setCapability("accessKey", sauceKey);
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("platformName", "Windows 10");
        capabilities.setCapability("sauce:options", sauceOptions);
        return capabilities;
    }

    private WebDriver createWebdriver(BrowserType type) {
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
                map.map.put(BrowserType.CHROME, createWebdriver(BrowserType.CHROME));
            } else {
                LOGGER.info(MyMarkers.DRIVER,
                        "thread id {} does not contain the browser type {}",
                        Thread.currentThread().getId(), type.toString());
                map.map.put(type, createWebdriver(type));
            }
        } else {
            BrowserMap map = threadLocal.get();
            if (!map.map.containsKey(type)) {
                LOGGER.info(MyMarkers.DRIVER,
                        "thread id {} does not contain the browser type {}",
                        Thread.currentThread().getId(), type.toString());
                map.map.put(type, createWebdriver(type));
            }
        }
        return threadLocal.get().map.get(type);
    }


}
