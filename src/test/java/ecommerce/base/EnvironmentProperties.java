package ecommerce.base;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

public class EnvironmentProperties {

    public static final String ENV_FILE = "src//test//resources//env.properties";
    private static final String RESOURCES_FOLDER = "src//test//resources";

    public static final String KEY_BROWSER = "browser";
    public static final String KEY_URL = "url";
    public static final String KEY_REGISTRATION_FILE = "registration_spreadsheet";

    /** For the registration test scenario, adds a suffix to make the email unique.
     *  This is to ensure that the email address does not already exist.
     */
    public static final String KEY_REGISTRATION_EMAIL_SUFFIX = "registration_email_suffix";

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
        } else {
            LOGGER.info("The url value specified is : " + urlValue);
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

    public static EnvironmentProperties getInstance() {
        return INSTANCE;
    }

}
