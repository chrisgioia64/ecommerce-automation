package ecommerce.base;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.AssertJUnit.assertEquals;

public class SauceLabsDriver {

    private WebDriver driver;

    @Before
    public void setup() throws MalformedURLException {
        MutableCapabilities sauceOptions = new MutableCapabilities();
        String sauceUser = EnvironmentProperties.getInstance().getProperty(EnvironmentProperties.SAUCE_USERNAME);
        String sauceKey = EnvironmentProperties.getInstance().getProperty(EnvironmentProperties.SAUCE_ACCESS_KEY);
        sauceOptions.setCapability("username", sauceUser);
        sauceOptions.setCapability("accessKey", sauceKey);
        MutableCapabilities capabilities = new MutableCapabilities();
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "98.0");
        capabilities.setCapability("platformName", "Windows 10");
        capabilities.setCapability("sauce:options", sauceOptions);
        String sauceUrl = String.format(" https://ondemand.us-west-1.saucelabs.com/wd/hub");
        driver = new RemoteWebDriver(new URL(sauceUrl), capabilities);
    }

    @Test
    public void test() {
        driver.get("http://www.automationexercise.com");
        System.out.println(driver.getTitle());
        assertEquals(driver.getTitle(), "Automation Exercise");
    }

}
