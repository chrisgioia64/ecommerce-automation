package ecommerce.pages;

import ecommerce.base.MyMarkers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

/**
 * Utility class for methods that are common across pages (e.g. header)
 *
 */
public class PageUtils {

    private static final Logger LOGGER = LogManager.getLogger(PageUtils.class);

    /**
     * @param priceText text of the form "Rs. 800"
     * @return the price in rupees (the integer extracted from the text)
     */
    public static int extractPrice(String priceText) {
        String[] s = priceText.split("\\s+");
        try {
            return Integer.parseInt(s[1]);
        } catch (NumberFormatException e) {
            LOGGER.warn(MyMarkers.PAGE_OBJECTS,
                    "Could not parse price from priceText '{}'", priceText);
            return 0;
        }
    }

}