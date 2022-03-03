package ecommerce.pages;

import ecommerce.base.EnvironmentProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * The base page object for all pages on the ecommerce automation site.
 * Shares common structure such as the header links.
 */
public abstract class AutomationExercisePage extends PageObject {

    private static final String HOME_LINK = "Home";
    private static final String PRODUCT_LINK = "Products";
    private static final String CART_LINK = "Cart";
    private static final String SIGNUP_LINK = "Signup";
    public static final String LOGOUT_LINK = "Logout";
    private static final String DELETE_LINK = "Delete Account";

    public AutomationExercisePage(WebDriver driver) {
        super(driver);
    }

    public void clickHomeLink() {
        clickLink(HOME_LINK);
    }

    public void clickProductLink() {
        clickLink(PRODUCT_LINK);
    }

    public void clickCartLink() {
        clickLink(CART_LINK);
    }

    public void clickSignupLink() {
        clickLink(SIGNUP_LINK);
    }

    public void clickLogoutLink() {
        clickLink(LOGOUT_LINK);
    }

    public void clickLogoutLinkIfExists() {
        clickLink(LOGOUT_LINK, false);
    }

    public void clickDeleteLink() {
        clickLink(DELETE_LINK);
    }

    private void clickLink(String partialText, boolean throwError) {
        try {
            WebElement element = driver.findElement(By.partialLinkText(partialText));
            element.click();
        } catch (NoSuchElementException ex) {
            if (throwError) {
                throw ex;
            }
        }
    }

    private void clickLink(String partialText) {
        clickLink(partialText, true);
    }

    public boolean linkExists(String partialText) {
        try {
            WebElement element = driver.findElement(By.partialLinkText(partialText));
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    /** Navigates to the url of the given page.
     *  Throws an exception when additional information (e.g. product ID) is needed
     */
    public abstract void navigateToPage();

    private String getFullUrl(String subUrl) {
        return EnvironmentProperties.getInstance().getUrl() + "/" + subUrl;
    }

    protected void navigateToSuburl(String subUrl) {
        String fullUrl = getFullUrl(subUrl);
        driver.get(fullUrl);
    }

}
