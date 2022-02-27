package ecommerce.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AutomationExercisePage extends PageObject {

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

    public void clickDeleteLink() {
        clickLink(DELETE_LINK);
    }

    private void clickLink(String partialText) {
        WebElement element = driver.findElement(By.partialLinkText(partialText));
        element.click();
    }

    public boolean linkExists(String partialText) {
        try {
            WebElement element = driver.findElement(By.partialLinkText(partialText));
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

}
