package ecommerce.pageObjects;

import ecommerce.features.registration.RegisteredUser;
import ecommerce.features.registration.RegistrationSpreadsheet;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SignupPage extends AutomationExercisePage {

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
        WebElement nameElement = driver.findElement(By.cssSelector(NAME_FIELD));
        nameElement.clear();
        nameElement.sendKeys(user.getName());

        WebElement passwordElement = driver.findElement(By.cssSelector(PASSWORD_FIELD));
        passwordElement.sendKeys(user.getPassword());

        enterDob(user.getDay(), user.getMonth(), user.getYear());

        WebElement firstNameElement = driver.findElement(By.cssSelector(FIRST_NAME_FIELD));
        firstNameElement.sendKeys(user.getFirstName());

        WebElement lastNameElement = driver.findElement(By.cssSelector(LAST_NAME_FIELD));
        lastNameElement.sendKeys(user.getLastName());

        WebElement companyElement = driver.findElement(By.cssSelector(COMPANY_FIELD));
        companyElement.sendKeys(user.getCompany());

        WebElement address1Element = driver.findElement(By.cssSelector(ADDRESS_1_FIELD));
        address1Element.sendKeys(user.getAddress1());

        WebElement address2Element = driver.findElement(By.cssSelector(ADDRESS_2_FIELD));
        address2Element.sendKeys(user.getAddress2());

        enterCountry(user.getCountry());

        WebElement stateElement = driver.findElement(By.cssSelector(STATE_FIELD));
        stateElement.sendKeys(user.getState());

        WebElement cityElement = driver.findElement(By.cssSelector(CITY_FIELD));
        cityElement.sendKeys(user.getCity());

        WebElement zipElement = driver.findElement(By.cssSelector(ZIP_FIELD));
        zipElement.sendKeys(user.getZipCode());

        WebElement mobileElement = driver.findElement(By.cssSelector(MOBILE_FIELD));
        mobileElement.sendKeys(user.getMobileNumber());

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
