package ecommerce.automation.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentProperties {

    public static final String ENV_FILE = "src//test//resources//env.properties";

    public static final String KEY_BROWSER = "browser";
    public static final String KEY_URL = "url";

    private Properties prop;

    private void initializeProperties() {
        this.prop = new Properties();
        try {
            prop.load(new FileInputStream(ENV_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return prop.get(key).toString();
    }

    public static void main(String[] args) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(ENV_FILE));
            for (Object o : prop.keySet()) {
                System.out.println(o + " " + prop.get(o));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
