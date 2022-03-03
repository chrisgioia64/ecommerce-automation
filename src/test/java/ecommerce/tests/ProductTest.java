package ecommerce.tests;

import ecommerce.api.APIUtils;
import ecommerce.api.EcommerceApiException;
import ecommerce.base.BaseTest;
import ecommerce.base.EnvironmentProperties;
import ecommerce.pages.ProductDetailsPage;
import ecommerce.scenarios.product.ProductInformation;
import ecommerce.scenarios.product.ProductList;
import ecommerce.scenarios.product.ProductUtils;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.apiguardian.api.API;
import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.AssertJUnit.assertEquals;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.*;


public class ProductTest extends BaseTest {

    private final static Logger LOGGER = Logger.getLogger(ProductTest.class);

    /**
     * Use API 1 (Get all products) to retrieve information about all products.
     * Save in a Java Object. Check the product details page and see if the
     * API matches what’s on the page. (combination of front-end and API testing)
     */
    @Test(dataProvider = "productTestCases")
    public void api1(ProductInformation product) {
        String url = EnvironmentProperties.getInstance().getUrl() + "/"
                + ProductDetailsPage.PAGE_URL + "/" + product.getId();
        WebDriver driver = getDriver();
        driver.get(url);

        ProductDetailsPage detailsPage = new ProductDetailsPage(driver);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(detailsPage.getProductName(), product.getName());
        softAssert.assertEquals(detailsPage.parsePrice(), product.getPrice(), 1);
        softAssert.assertEquals(detailsPage.parseOuterCategory(), product.getOuterCategory());
        softAssert.assertEquals(detailsPage.parseInnerCategory(), product.getInnerCategory());
        softAssert.assertEquals(detailsPage.parseBrand(), product.getBrand());
        softAssert.assertAll();
    }

    @DataProvider(name = "productTestCases")
    public Object[][] getProductList() {
        Response r = APIUtils.getResponseProductList();
        List<ProductInformation> productList = new LinkedList<>();
        try {
            productList = APIUtils.extractJson(r, ProductUtils::extractProductList).getProductList();
        } catch (EcommerceApiException ex) {
            LOGGER.info("API Exception with retrieving product list " + ex.getMessage());
        } catch (JSONException ex) {
            LOGGER.info("Could not parse json response from product list " + ex.getMessage());
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
    @Test
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
    @Test(dataProvider = "getAllBrands")
    public void api3(Integer id, String brand) {
        String url = EnvironmentProperties.getInstance().getUrl() + "/"
                + ProductDetailsPage.PAGE_URL + "/" + id;
        WebDriver driver = getDriver();
        driver.get(url);

        ProductDetailsPage detailsPage = new ProductDetailsPage(driver);
        assertEquals(detailsPage.parseBrand(), brand);
    }

    @DataProvider(name = "getAllBrands")
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
    @Test
    public void api4() {
        Response response = APIUtils.putResponseBrandList();
        String jsonResponse = response.asString();
        JSONObject obj = new JSONObject(jsonResponse);
        Integer responseCode = obj.getInt("responseCode");
        String message = obj.getString("message");
        assertEquals("This request method is not supported.", message);
    }

}
