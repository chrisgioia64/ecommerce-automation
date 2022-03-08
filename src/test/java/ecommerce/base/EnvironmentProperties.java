package ecommerce.base;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentProperties {

//    public static final String ENV_FILE = "src//test//resources//dev.properties";
    private static final String RESOURCES_FOLDER = "src//test//resources//";

    /** The default browser to use for testing. */
    public static final String KEY_BROWSER = "browser";

    /** The base url for the AUT (Application Under Test). */
    public static final String KEY_URL = "url";

    /** The location of the spreadsheet used for doing data-driven testing
     * for the registration and login test cases
     */
    public static final String KEY_REGISTRATION_FILE = "registration_spreadsheet";

    /**
     * For APIs 1 and 3, we test that the API results match the front-end.
     * However, rather than testing the results for all products, we only test
     * for products 1 through NUM_PRODUCTS. */
    public static final String KEY_NUM_PRODUCTS = "num_products";

    /**
     *  For the registration test scenario, adds a suffix to make the email unique.
     *  This is to ensure that the email address does not already exist.
     */
    public static final String KEY_REGISTRATION_EMAIL_SUFFIX = "registration_email_suffix";

    /**
     * An integer representing the number of seconds we should wait between cucumber steps
     * During production, value should be 0. Set to non-zero number for debugging purposes
     */
    public static final String KEY_STEP_WAIT = "cucumber_step_wait";

    /**
     * A boolean flag ("true" or "false") indicating whether we should perform
     * cross browser on a representative set of test cases (currently includes
     * front-end product scenarios for API 1 and API 3).
     * Right now, only perform cross browser testing using the data-driven testing
     * methodology. Perhaps there is a better way.
     */
    public static final String KEY_CROSS_BROWSER = "cross_browser";

    public static final String SAUCE_USERNAME = "sauce_username";

    public static final String SAUCE_ACCESS_KEY = "sauce_accessKey";

    /**
     * A boolean flag ("true" or "false") indicating whether we should Saucelabs
     * and remote web drivers instead of using local drivers
     */
    public static final String KEY_USE_SAUCELABS = "use_saucelabs";

    /**
     * A boolean flag ("true" or "false") indicating whether we should
     * use multiple drivers
     */
    public static final String KEY_MULTIPLE_DRIVERS = "multiple_drivers";

    private static final Logger LOGGER = LogManager.getLogger(EnvironmentProperties.class);

    private Properties prop;
    private final static EnvironmentProperties INSTANCE = new EnvironmentProperties();

    private Properties sauceProperties;

    private EnvironmentProperties() {
        initializeProperties();
    }

    private void initializeProperties() {
        this.prop = new Properties();
        String envFile = getEnvironmentFile();
        try {
            prop.load(new FileInputStream(envFile));
        } catch (IOException e) {
            LOGGER.error(MyMarkers.ENVIRONMENT_FILE,
                    "Could not load environment file: " + envFile);
            e.printStackTrace();
        }
    }

    private final static String ENVIRONMENT_FILE_SYSTEM_PROPERTY = "env_file";

    private final static String DEV_ENV_FILE = "dev.properties";

    public static String getEnvironmentFile() {
        String envFile = System.getProperty(ENVIRONMENT_FILE_SYSTEM_PROPERTY);
        if (envFile == null) {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "No env_file system property specified. Using " + DEV_ENV_FILE);
            return RESOURCES_FOLDER + DEV_ENV_FILE;
        }
        LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                "Using the following environment file {} ", envFile);
        return RESOURCES_FOLDER + envFile;
    }

    public String getProperty(String key) {
        if (prop.get(key) == null) {
            return null;
        }
        return prop.get(key).toString();
    }

    public String getRegistrationExcelFileLocation() {
        String location = getProperty(KEY_REGISTRATION_FILE);
        if (location == null) {
            LOGGER.error(MyMarkers.ENVIRONMENT_FILE,
                    "No file specified for registration excel spreadsheet " +
                    "inside environment file");
        }
        return RESOURCES_FOLDER + "//" + location;
    }

    public BrowserType getBrowserType() {
        String browser = getProperty(KEY_BROWSER);
        BrowserType result = null;
        for (BrowserType value : BrowserType.values()) {
            if (value.getName().equalsIgnoreCase(browser)) {
                result = value;
            }
        }
        if (result == null) {
            LOGGER.error(MyMarkers.ENVIRONMENT_FILE,
                    "The browser type specified in " +
                    "the properties file \"" + browser + "\" has no corresponding browser type");
        } else {
            LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                    "The browser type specified is: " + browser);
        }
        return result;
    }

    public String getUrl() {
        String urlValue = getProperty(KEY_URL);
        if (urlValue == null) {
            LOGGER.error(MyMarkers.ENVIRONMENT_FILE,
                    "There is no url value specified in the properties file");
        }
        return urlValue;
    }

    public String getEmailSuffix() {
        String emailSuffix = getProperty(KEY_REGISTRATION_EMAIL_SUFFIX);
        if (emailSuffix == null) {
            LOGGER.error(MyMarkers.ENVIRONMENT_FILE,
                    "There is no email suffix value specified in the properties file");
        } else {
            LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                    "The email suffix value is " + emailSuffix);
        }
        return emailSuffix;
    }

    public int getNumProducts() {
        String numProducts = getProperty(KEY_NUM_PRODUCTS);
        if (numProducts == null) {
            LOGGER.error(MyMarkers.ENVIRONMENT_FILE,
                    "There is no numProducts property specified");
            return 0;
        }
        try {
            int result = Integer.parseInt(numProducts);
            LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                    "The numProducts property is set to " + result);
            return result;
        } catch (NumberFormatException e) {
            LOGGER.error(MyMarkers.ENVIRONMENT_FILE,
                    "The numProducts property is not an integer");
        }
        return 0;
    }

    public double getCucumberWait() {
        String cucumberWaitString = getProperty(KEY_STEP_WAIT);
        if (cucumberWaitString == null) {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "No step wait specified in properties file");
            return 0;
        }
        try {
            double seconds = Double.parseDouble(cucumberWaitString);
            LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                    "Cucumber step wait specified in seconds as " + seconds);
            return seconds;
        } catch (NumberFormatException e) {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "invalid step wait specified: \"" + cucumberWaitString + "\"");
        }
        return 0.0;
    }

    public boolean isCrossBrowserTesting() {
        String flag = getProperty(KEY_CROSS_BROWSER);
        if (flag == null) {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "No cross_browser flag specified. Defaulting to false");
            return false;
        }
        if (flag.equalsIgnoreCase("true")) {
            return true;
        } else if (flag.equalsIgnoreCase("false")) {
            return false;
        } else {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "The cross_browser flag must be either true or false. Defaulting to false");
            return false;
        }
    }

    public boolean isSauceLabs() {
        String flag = getProperty(KEY_USE_SAUCELABS);
        if (flag == null) {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "No use_saucelabs flag specified. Defaulting to false");
            return false;
        }
        if (flag.equalsIgnoreCase("true")) {
            LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                    "use_saucelabs flag set to true");
            return true;
        } else if (flag.equalsIgnoreCase("false")) {
            LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                    "use_saucelabs flag se to false");
            return false;
        } else {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "use_saucelabs flag must be true or false. Defaulting to false");
            return false;
        }
    }

    public boolean isMultipleDrivers() {
        String flag = getProperty(KEY_MULTIPLE_DRIVERS);
        if (flag == null) {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "No multiple_drivers flag specified. Defaulting to false");
            return false;
        }
        if (flag.equalsIgnoreCase("true")) {
            LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                    "multiple_drivers flag set to true");
            return true;
        } else if (flag.equalsIgnoreCase("false")) {
            LOGGER.info(MyMarkers.ENVIRONMENT_FILE,
                    "multiple_drivers flag set to false");
            return false;
        } else {
            LOGGER.warn(MyMarkers.ENVIRONMENT_FILE,
                    "multiple_drivers flag must be true or false. Defaulting to false");
            return false;
        }
    }

    public static EnvironmentProperties getInstance() {
        return INSTANCE;
    }

}
