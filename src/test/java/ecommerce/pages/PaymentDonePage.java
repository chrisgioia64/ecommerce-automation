package ecommerce.pages;

import org.openqa.selenium.WebDriver;

public class PaymentDonePage extends AutomationExercisePage {

    public static final String DOWNLOAD_INVOICE_LINK_TEXT = "Download Invoice";

    public PaymentDonePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void navigateToPage() {
        throw new IllegalArgumentException("cannot directly navigate to PaymentDone page");
    }

    public boolean downloadInvoiceButtonExists() {
        return linkExists(DOWNLOAD_INVOICE_LINK_TEXT);
    }
}
