package ecommerce.base;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class EnvironmentProperties {

    public static final String ENV_FILE = "src//test//resources//env.properties";
    private static final String RESOURCES_FOLDER = "src//test//resources";

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

    private static final Logger LOGGER = Logger.getLogger(EnvironmentProperties.class);
    private Properties prop;
    private final static EnvironmentProperties INSTANCE = new EnvironmentProperties();

    private EnvironmentProperties() {
        initializeProperties();
    }

    private void initializeProperties() {
        this.prop = new Properties();
        try {
            prop.load(new FileInputStream(ENV_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getProperty(String key) {
        if (prop.get(key) == null) {
            return null;
        }
        return prop.get(key).toString();
    }

    public String getRegistrationExcelFileLocation() {
        String location = getProperty(KEY_REGISTRATION_FILE);
        if (location == null) {
            LOGGER.error("No file specified for registration excel spreadsheet " +
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
            LOGGER.error("The browser type specified in " +
                    "the properties file \"" + browser + "\" has no corresponding browser type");
        } else {
            LOGGER.info("The browser type specified is: " + browser);
        }
        return result;
    }

    public String getUrl() {
        String urlValue = getProperty(KEY_URL);
        if (urlValue == null) {
            LOGGER.error("There is no url value specified in the properties file");
        }
        return urlValue;
    }

    public String getEmailSuffix() {
        String emailSuffix = getProperty(KEY_REGISTRATION_EMAIL_SUFFIX);
        if (emailSuffix == null) {
            LOGGER.error("There is no email suffix value specified in the properties file");
        } else {
            LOGGER.info("The email suffix value is " + emailSuffix);
        }
        return emailSuffix;
    }

    public int getNumProducts() {
        String numProducts = getProperty(KEY_NUM_PRODUCTS);
        if (numProducts == null) {
            LOGGER.error("There is no numProducts property specified");
            return 0;
        }
        try {
            int result = Integer.parseInt(numProducts);
            LOGGER.info("The numProducts property is set to " + result);
            return result;
        } catch (NumberFormatException e) {
            LOGGER.error("The numProducts property is not an integer");
        }
        return 0;
    }

    public int getCucumberWait() {
        String cucumberWaitString = getProperty(KEY_STEP_WAIT);
        if (cucumberWaitString == null) {
            LOGGER.warn("No step wait specified in properties file");
            return 0;
        }
        try {
            int seconds = Integer.parseInt(cucumberWaitString);
            LOGGER.info("Cucumber step wait specified in seconds as " + seconds);
            return seconds;
        } catch (NumberFormatException e) {
            LOGGER.warn("invalid step wait specified: \"" + cucumberWaitString + "\"");
        }
        return 0;
    }

    public boolean isCrossBrowserTesting() {
        String flag = getProperty(KEY_CROSS_BROWSER);
        if (flag == null) {
            LOGGER.warn("No cross_browser flag specified. Defaulting to false");
            return false;
        }
        if (flag.equalsIgnoreCase("true")) {
            return true;
        } else if (flag.equalsIgnoreCase("false")) {
            return false;
        } else {
            LOGGER.warn("The cross_browser flag must be either true or false. Defaulting to false");
            return false;
        }
    }

    public static EnvironmentProperties getInstance() {
        return INSTANCE;
    }

}
