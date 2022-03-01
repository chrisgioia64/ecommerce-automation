package ecommerce.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * This is the base page object classes from which all page objects extend
 */
public abstract class PageObject {

    protected final WebDriver driver;

    private static final Logger LOGGER = Logger.getLogger(PageObject.class);

    public PageObject(WebDriver driver) {
        this.driver = driver;
    }

    protected void selectByValue(WebElement element, String value, String elementName) {
        Select select = new Select(element);
        try {
            select.selectByValue(value);
        } catch (NoSuchElementException ex) {
            LOGGER.info("The select element \"" + elementName + "\" does not have option " +
                    "with value \"" + value + "\"");
        }
    }

}
