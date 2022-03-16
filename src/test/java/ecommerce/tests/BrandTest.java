package ecommerce.tests;

import ecommerce.base.BaseTest;
import ecommerce.base.BrowserType;
import ecommerce.base.EnvironmentProperties;
import ecommerce.base.MyMarkers;
import ecommerce.pages.BrandProductsPage;
import ecommerce.pages.ProductDetailsPage;
import ecommerce.pages.ProductsPage;
import ecommerce.reports.ExtentTestListener;
import ecommerce.scenarios.product.ProductInformation;
import io.cucumber.java.it.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;

public class BrandTest extends BaseTest {

    private final static Logger LOGGER = LogManager.getLogger(BrandTest.class);

    /**
     * Verify that the count displayed next to each brand on the sidebar
     * shows the correct numbers of products
     */
    @Test(dataProvider = "brands",
            groups = {TestGroups.FRONTEND, TestGroups.BRAND})
    public void verifyBrandCountCorrect(String brandName, String suburl, int count) {
        WebDriver driver = getDriver();
        driver.get(suburl);
        BrandProductsPage page = new BrandProductsPage(driver);

        LOGGER.info(MyMarkers.TEST,
                "Verifying that brand {} has {} products", brandName, count);
        assertEquals(count, page.getProductNames().size());
    }

    /**
     * Verify that the brand name (as displayed in left sidebar) matches the
     * breadcrumbs at the top of the page
     */
    @Test(dataProvider = "brands",
            groups={TestGroups.FRONTEND, TestGroups.BRAND})
    public void verifyBreadcrumbs(String brandName, String suburl, int count) {
        WebDriver driver = getDriver();
        driver.get(suburl);
        BrandProductsPage page = new BrandProductsPage(driver);

        LOGGER.info(MyMarkers.TEST,
                "Verifying breadcrumbs for brand name {}", brandName);
        assertThat(page.getBrandBreadcrumb(), Matchers.equalToIgnoringCase(brandName));
    }

    /**
     * Verify that each of the products listed for a particular brand has
     * the correct brand listed on their product details page
     */
    @Test(dataProvider = "brands",
            groups={TestGroups.FRONTEND, TestGroups.BRAND})
    public void verifyProductDetails(String brandName, String suburl, int count) {
        WebDriver driver = getDriver();
        driver.get(suburl);
        BrandProductsPage page = new BrandProductsPage(driver);
        ProductDetailsPage detailsPage = new ProductDetailsPage(driver);

        LOGGER.info(MyMarkers.TEST,
                "Verifying product details for products with brand name {}", brandName);
        for (String productName : page.getProductNames()) {
            boolean b = page.viewProductDetails(productName);
            if (!b) {
                fail("Could not select product details for " + productName);
            }
            LOGGER.info(MyMarkers.TEST,
                    "Verifying that product {} is of brand {}", productName, brandName);
            assertThat(detailsPage.parseBrand(), Matchers.equalToIgnoringCase(brandName));
            driver.get(suburl);
        }
    }

    @DataProvider(name="brands")
    public Object[][] getBrands() {
        WebDriver driver = getDriver();
        ProductsPage productsPage = new ProductsPage(driver);
        productsPage.navigateToPage();
        List<ProductsPage.BrandInfo> list = productsPage.getBrandInfo()
                .subList(0, EnvironmentProperties.getInstance().getNumProducts());
        int index = 0;
        Object[][] result = new Object[list.size()][3];
        for (ProductsPage.BrandInfo info : list) {
            result[index][0] = info.brandName;
            result[index][1] = info.brandUrl;
            result[index][2] = info.count;
            index++;
        }
        LOGGER.info("Collected {} brands for the data provider", result.length);
        return result;
    }


    /**
     * Verify that this non-existent brand has no products associated with it
     */
    @Test(dataProvider = "non-brands",
            groups = {TestGroups.FRONTEND, TestGroups.BRAND})
    public void verifyNonexistentBrand(String brandName) {
        WebDriver driver = getDriver();
        String url = EnvironmentProperties.getInstance().getUrl() + "/"
                + BrandProductsPage.PAGE_URL + "/" + brandName;
        driver.get(url);
        BrandProductsPage page = new BrandProductsPage(driver);

        LOGGER.info(MyMarkers.TEST,
                "Verifying that brand {} has {} products", brandName, 0);
        assertEquals(0, page.getProductNames().size());
    }

    @DataProvider(name="non-brands")
    public Object[][] getNonExistentBrands() {
        List<String> list = Arrays.asList("Nike", "polo");
        int index = 0;
        Object[][] result = new Object[list.size()][1];
        for (String brand : list) {
            result[index][0] = brand;
            index++;
        }
        LOGGER.info("Collected {} non-existent brands for the data provider", result.length);
        return result;
    }

}
