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
import org.hamcrest.Matchers;
import org.hamcrest.Matchers.*;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.LinkedList;
import java.util.List;

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
    public void productTest(ProductInformation product) {
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

}
