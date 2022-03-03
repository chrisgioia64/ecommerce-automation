package ecommerce.pages;

import ecommerce.scenarios.registration.RegisteredUser;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Registration Signup page
 * https://www.automationexercise.com/signup
 */
public class SignupPage extends AutomationExercisePage {

    public static final String URL = "signup";

    public static final String NAME_FIELD = "#name";
    private static final String PASSWORD_FIELD = "#password";
    private static final String DAYS_FIELD = "#days";
    private static final String MONTHS_FIELD = "#months";
    private static final String YEARS_FIELD = "#years";

    private static final String FIRST_NAME_FIELD = "#first_name";
    private static final String LAST_NAME_FIELD = "#last_name";
    private static final String COMPANY_FIELD = "#company";
    private static final String ADDRESS_1_FIELD = "#address1";
    private static final String ADDRESS_2_FIELD = "#address2";
    private static final String COUNTRY_FIELD = "#country";
    private static final String STATE_FIELD = "#state";
    private static final String CITY_FIELD = "#city";
    private static final String ZIP_FIELD = "#zipcode";
    private static final String MOBILE_FIELD = "#mobile_number";

    private static final String SUBMIT_BUTTON = "form[action='/signup'] button[type='submit']";

    /** The css selector for the 'Continue' button that appears on the success page
     * when an account is successfully created
     */
    private static final String CONTINUE_BUTTON = "a[data-qa='continue-button']";

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void navigateToPage() {
        navigateToSuburl(URL);
    }

    private void enterDob(int day, String month, int year) {
        WebElement dayElement = driver.findElement(By.cssSelector(DAYS_FIELD));
        selectByValue(dayElement, day + "", "day");

        WebElement monthElement = driver.findElement(By.cssSelector(MONTHS_FIELD));
        selectByValue(monthElement, month, "month");

        WebElement yearElement = driver.findElement(By.cssSelector(YEARS_FIELD));
        selectByValue(yearElement, year + "", "year");
    }

    private void enterCountry(String country) {
        WebElement element = driver.findElement(By.cssSelector(COUNTRY_FIELD));
        selectByValue(element, country, "country");
    }

    public void submit() {
        WebElement element = driver.findElement(By.cssSelector(SUBMIT_BUTTON));
        element.click();
    }

    public void enterRegistration(RegisteredUser user) {
        sendKeys(NAME_FIELD, user.getName());
        sendKeys(PASSWORD_FIELD, user.getPassword());
        enterDob(user.getDay(), user.getMonth(), user.getYear());

        sendKeys(FIRST_NAME_FIELD, user.getFirstName());
        sendKeys(LAST_NAME_FIELD, user.getLastName());
        sendKeys(COMPANY_FIELD, user.getCompany());
        sendKeys(ADDRESS_1_FIELD, user.getAddress1());
        sendKeys(ADDRESS_2_FIELD, user.getAddress2());
        enterCountry(user.getCountry());
        sendKeys(STATE_FIELD, user.getState());
        sendKeys(CITY_FIELD, user.getCity());
        sendKeys(ZIP_FIELD, user.getZipCode());
        sendKeys(MOBILE_FIELD, user.getMobileNumber());
    }

    /**
     * If the 'Continue' button, then we are on the page that appears when an
     * account is successfully created
     */
    public boolean onAccountSuccessPage() {
        try {
            WebElement element = driver.findElement(By.cssSelector(CONTINUE_BUTTON));
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }
}
