package ecommerce.pages;

import org.openqa.selenium.WebDriver;

/**
 * Page Object for Payment Page (where credit card details are entered)
 * https://www.automationexercise.com/payment
 */
public class PaymentPage extends AutomationExercisePage {

    public static final String URL = "payment";

    public static final String CARD_NAME_SELECTOR = "form[action='/payment'] input[name='name_on_card']";
    public static final String CARD_NUMBER_SELECTOR = "form[action='/payment'] input[name='card_number']";
    public static final String CVC_SELECTOR = "form[action='/payment'] input[name='cvc']";
    public static final String MONTH_SELECTOR = "form[action='/payment'] input[name='expiry_month']";
    public static final String YEAR_SELECTOR = "form[action='/payment'] input[name='expiry_year']";

    public static final String SUBMIT_BUTTON_SELECTOR = "#submit";

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void navigateToPage() {
        navigateToSuburl(URL);
    }

    public void enterCardInformation(String name, String cardNumber, String cvc,
                                     String month, String year) {
        sendKeys(CARD_NAME_SELECTOR, name);
        sendKeys(CARD_NUMBER_SELECTOR, cardNumber);
        sendKeys(CVC_SELECTOR, cvc);
        sendKeys(MONTH_SELECTOR, month);
        sendKeys(YEAR_SELECTOR, year);
        click(SUBMIT_BUTTON_SELECTOR);
    }


}
