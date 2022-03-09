package ecommerce.tests;

import ecommerce.api.APIUtils;
import ecommerce.api.EcommerceApiException;
import ecommerce.base.BaseTest;
import ecommerce.base.BrowserType;
import ecommerce.base.CrossBrowserUtils;
import ecommerce.base.EnvironmentProperties;
import ecommerce.pages.ProductDetailsPage;
import ecommerce.pages.ProductsPage;
import ecommerce.scenarios.product.ProductInformation;
import ecommerce.scenarios.product.ProductUtils;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;


public class APITest extends BaseTest {

    private final static Logger LOGGER = LogManager.getLogger(APITest.class);

    /**
     * Use API 1 (Get all products) to retrieve information about all products.
     * Save in a Java Object. Check the product details page and see if the
     * API matches what’s on the page. (combination of front-end and API testing)
     */
    @Test(dataProvider = "productTestCases",
            groups = {TestGroups.API, TestGroups.FRONTEND, TestGroups.PRODUCT},
            description = "Use API to get all products and then verify with backend")
    public void api1(ProductInformation product, BrowserType browserType) {
        String url = EnvironmentProperties.getInstance().getUrl() + "/"
                + ProductDetailsPage.PAGE_URL + "/" + product.getId();
        WebDriver driver = getDriver(browserType);
        driver.get(url);
        LOGGER.info("Pre Verify between API and product details page for '{}'",
                product.toString());

        ProductDetailsPage detailsPage = new ProductDetailsPage(driver);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(detailsPage.getProductName(), product.getName());
        softAssert.assertEquals(detailsPage.parsePrice(), product.getPrice(), 1);
        softAssert.assertEquals(detailsPage.parseOuterCategory(), product.getOuterCategory());
        softAssert.assertEquals(detailsPage.parseInnerCategory(), product.getInnerCategory());
        softAssert.assertEquals(detailsPage.parseBrand(), product.getBrand());
        softAssert.assertAll();
        LOGGER.info("Successfully Verified between API and product details page for '{}'",
                product.toString());

    }

    @DataProvider(name = "productTestCases", parallel = true)
    public Object[][] getProductListOverBrowsers() {
        Object[][] data = getProductList();
        if (EnvironmentProperties.getInstance().isCrossBrowserTesting()) {
            return CrossBrowserUtils.enumerateOverAllBrowsers(data);
        } else {
            return CrossBrowserUtils.enumerateDefaultBrowser(data);
        }
    }

    public Object[][] getProductList() {
        Response r = APIUtils.getResponseProductList();
        List<ProductInformation> productList = new LinkedList<>();
        try {
            productList = APIUtils.extractJson(r, ProductUtils::extractProductList).getProductList();
        } catch (EcommerceApiException ex) {
            LOGGER.warn("API Exception with retrieving product list " + ex.getMessage());
        } catch (JSONException ex) {
            LOGGER.warn("Could not parse json response from product list " + ex.getMessage());
        }
        int size = Math.min(EnvironmentProperties.getInstance().getNumProducts(), productList.size());
        Object[][] res = new Object[size][1];
        int index = 0;
        for (ProductInformation productInformation : productList) {
            res[index++][0] = productInformation;
            if (index >= size) {
                break;
            }
        }
        return res;
    }

    /**
     * Test API 2 --- POST to All Products
     * (unsupported request method)
     * Test that the response code is 405
     */
    @Test(groups = {TestGroups.API, TestGroups.PRODUCT},
        description = "Verify that POST to all Products is an unsupported operation")
    public void api2() {
        Response response = APIUtils.postResponseProductList();
        String jsonResponse = response.asString();
        JSONObject obj = new JSONObject(jsonResponse);
        Integer responseCode = obj.getInt("responseCode");
        String message = obj.getString("message");
        assertEquals("This request method is not supported.", message);
    }

    /**
     * Test API 3 : Get All Brands
     */
    @Test(dataProvider = "getAllBrands",
        groups = {TestGroups.API, TestGroups.FRONTEND, TestGroups.BRAND},
        description = "API to backend test. Retrieve all brands from API and then verify " +
                "product details page contains the correct brand")
    public void api3(Integer id, String brand, BrowserType browserType) {
        String url = EnvironmentProperties.getInstance().getUrl() + "/"
                + ProductDetailsPage.PAGE_URL + "/" + id;
        WebDriver driver = getDriver(browserType);
        driver.get(url);

        ProductDetailsPage detailsPage = new ProductDetailsPage(driver);
        assertEquals(detailsPage.parseBrand(), brand);
    }

    @DataProvider(name = "getAllBrands", parallel = true)
    public Object[][] getAllBrandsOverBrowsers() {
        Object[][] data = getAllBrands();
        if (EnvironmentProperties.getInstance().isCrossBrowserTesting()) {
            return CrossBrowserUtils.enumerateOverAllBrowsers(data);
        } else {
            return CrossBrowserUtils.enumerateDefaultBrowser(data);
        }
    }

    public Object[][] getAllBrands() {
        Response r = APIUtils.getResponseBrandList();
        Map<Integer, String> map = new HashMap<>();
        try {
            map = APIUtils.extractJson(r, ProductUtils::getIdToBrandMap);
        } catch (EcommerceApiException ex) {
            LOGGER.info("API Exception with retrieving brand list " + ex.getMessage());
        } catch (JSONException ex) {
            LOGGER.info("Could not parse json response from brand list " + ex.getMessage());
        }
        int size = Math.min(EnvironmentProperties.getInstance().getNumProducts(), map.size());
        Object[][] res = new Object[size][2];
        int index = 0;
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            res[index][0] = entry.getKey();
            res[index][1] = entry.getValue();
            index++;
            if (index >= size) {
                break;
            }
        }
        return res;
    }

    /**
     * Test API 4 -- Put to All Brands (method unsupported)
     * Test that the response code is 405
     */
    @Test(groups = {TestGroups.API, TestGroups.BRAND},
        description = "Verify that PUT to all Brands is unsupported")
    public void api4() {
        Response response = APIUtils.putResponseBrandList();
        String jsonResponse = response.asString();
        JSONObject obj = new JSONObject(jsonResponse);
        Integer responseCode = obj.getInt("responseCode");
        String message = obj.getString("message");
        assertEquals("This request method is not supported.", message);
    }

    /**
     * Makes sure that the front-end and the back-end return the same product names
     * when performing a search query
     * @param searchQuery the product to search for (e.g. "top")
     */
    @Parameters({"searchQuery"})
    @Test(groups = {TestGroups.API, TestGroups.FRONTEND, TestGroups.PRODUCT},
        description="Verify that the front-end and backend match for search.")
    public void api5(String searchQuery) {
        // Backend call
        Response r = APIUtils.postSearchProduct(searchQuery);
        Set<String> productNamesAPI = new HashSet<>();
        try {
            productNamesAPI = APIUtils.extractJson(r, ProductUtils::getProductNamesFromSearch);
        } catch (EcommerceApiException ex) {
            String msg = "API Exception with search products " + ex.getMessage();
            LOGGER.info(msg);
            fail(msg);
        } catch (JSONException ex) {
            String msg = "Could not parse json response from search products " + ex.getMessage();
            LOGGER.info(msg);
            fail(msg);
        }

        // Frontend verification
        WebDriver driver = getDriver();
        ProductsPage page = new ProductsPage(driver);
        page.navigateToPage();
        page.searchAndSubmit(searchQuery);
        Set<String> productNamesOnPage = page.getProductNames();

        for (String productNameAPI : productNamesAPI) {
            if (!productNamesOnPage.contains(productNameAPI)) {
                fail("The following product should appear on the page: \""
                        + productNameAPI + "\"");
            }
        }
        for (String productNamePage : productNamesOnPage) {
            if (!productNamesAPI.contains(productNamePage)) {
                fail("The following product appears on the page but was not " +
                        "returned in the API \"" + productNamePage + "\"");
            }
        }
    }

}
