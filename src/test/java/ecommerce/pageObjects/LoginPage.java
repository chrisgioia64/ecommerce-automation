package ecommerce.pageObjects;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Login Page
 * https://www.automationexercise.com/login
 */
public class LoginPage extends AutomationExercisePage {

    public static final String PAGE_URL = "login";

    // Registration Form CSS Selectors
    private static final String REGISTER_NAME = "form[action='/signup'] input[name='name']";
    private static final String REGISTER_EMAIL = "form[action='/signup'] input[name='email']";
    private static final String REGISTER_BUTTON = "form[action='/signup'] button";

    // Login Form CSS Selectors
    private static final String LOGIN_EMAIL = "form[action='/login'] input[name='email']";
    private static final String LOGIN_PASSWORD = "form[action='/login'] input[name='password']";
    private static final String LOGIN_BUTTON = "form[action='/login'] button";

    private static final Logger LOGGER = Logger.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Enters username and email into the "New User Signup" form
     * @param username the desired username of the new account
     * @param email the desired email of the new account
     * @return true if we are redirected to the signup form, false
     * if we are not (e.g. the email address already exists)
     */
    public boolean enterRegistration(String username, String email) {
        WebElement usernameElement = driver.findElement(By.cssSelector(REGISTER_NAME));
        WebElement emailElement = driver.findElement(By.cssSelector(REGISTER_EMAIL));
        WebElement submit = driver.findElement(By.cssSelector(REGISTER_BUTTON));
        usernameElement.sendKeys(username);
        emailElement.sendKeys(email);
        LOGGER.info("Enter into registration, name = \"" + username + "\" " +
                " and email = \"" + email + "\"");
        submit.click();
        try {
            driver.findElement(By.cssSelector(SignupPage.NAME_FIELD));
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    /**
     * Enters email and password into the Login form
     * @param email the email of the account
     * @param password the password of the account
     * @return
     */
    public boolean enterLogin(String email, String password) {
        WebElement emailElement = driver.findElement(By.cssSelector(LOGIN_EMAIL));
        WebElement passwordElement = driver.findElement(By.cssSelector(LOGIN_PASSWORD));
        WebElement submit = driver.findElement(By.cssSelector(LOGIN_BUTTON));
        emailElement.sendKeys(email);
        passwordElement.sendKeys(password);
        LOGGER.info("Enter into login, email = \"" + email + "\" " +
                " and password \"" + password + "\"");
        submit.click();
        return linkExists(AutomationExercisePage.LOGOUT_LINK);
    }
}
