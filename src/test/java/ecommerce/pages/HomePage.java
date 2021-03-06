package ecommerce.pages;

import org.openqa.selenium.WebDriver;

/**
 * Page Object for Home Page
 * https://www.automationexercise.com/
 */
public class HomePage extends AutomationExercisePage {

    public static final String URL = "";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void navigateToPage() {
        navigateToSuburl(URL);
    }

}
