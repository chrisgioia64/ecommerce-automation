package ecommerce.api;

import ecommerce.scenarios.product.ProductList;
import ecommerce.scenarios.product.ProductUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import static io.restassured.RestAssured.given;

/**
 * Utility methods for performing API requests
 * See the list of API methods at:
 * https://www.automationexercise.com/api_list
 */
public class APIUtils {

    public static final Logger LOGGER = LogManager.getLogger(APIUtils.class);

    /**
     * API 7 / API 10
     * Returns the response object from calling verify login
     */
    private static Response getResponseVerifyLogin(String email, String password) {
        LOGGER.info("Calling API 7 -- Verify Login with email {} and password {}", email, password);
        return given().relaxedHTTPSValidation()
                .contentType(ContentType.URLENC)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("api/verifyLogin")
                .then()
                .extract().response();
    }

    /**
     * API 1 : Get all products list
     */
    public static Response getResponseProductList() {
        LOGGER.info("Calling API 1 -- Get Product List");
        return given().relaxedHTTPSValidation()
                .when()
                .get("api/productsList")
                .then()
                .extract().response();
    }

    /**
     * API 2 -- Post to all products list
     */
    public static Response postResponseProductList() {
        LOGGER.info("Calling API 2 -- POST Product List");
        return given().relaxedHTTPSValidation()
                .when()
                .post("api/productsList")
                .then()
                .extract().response();
    }

    /**
     * API 3 - Get all brands list
     */
    public static Response getResponseBrandList() {
        LOGGER.info("Calling API 3 -- Get All Brands");
        return given().relaxedHTTPSValidation()
                .when()
                .get("api/brandsList")
                .then()
                .extract().response();
    }

    /**
     * API 4 - Put to all brands
     */
    public static Response putResponseBrandList() {
        LOGGER.info("Calling API 4 -- Put All Brands");
        return given().relaxedHTTPSValidation()
                .when()
                .put("api/brandsList")
                .then()
                .extract().response();
    }

    /**
     * API 5 -- POST to Search Product
     */
    public static Response postSearchProduct(String searchQuery) {
        LOGGER.info("Calling API 5 -- POST all products with product {}", searchQuery);
        return given().relaxedHTTPSValidation()
                .contentType(ContentType.URLENC)
                .formParam("search_product", searchQuery)
                .when()
                .post("api/searchProduct")
                .then()
                .extract().response();
    }

    public static <T> T extractJson(Response response, JsonExtractor<T> extractor)
            throws EcommerceApiException {
        if (response.getStatusCode() != 200) {
            throw new EcommerceApiException("Invalid status code for response : "
                + response.getStatusCode());
        }
        String jsonString = response.asString();
        return extractor.extract(jsonString);
    }

    private static final String LOGIN_SUCCESS = "User exists!";
    private static final String LOGIN_UNSUCCESSFUL = "User not found!";

    /**
     * Performs an API request on the verify login API
     */
    public static boolean loginSuccessful(String email, String password) throws EcommerceApiException {
        Response response = getResponseVerifyLogin(email, password);
        LOGGER.info(String.format("Login API with email %s and password %s", email, password));
        if (response.getStatusCode() != 200) {
            throw new EcommerceApiException("Login API returned status code " + response.getStatusCode());
        }
        String msg = response.jsonPath().getString("message");
        if (msg.equals(LOGIN_SUCCESS)) {
            return true;
        } else if (msg.equals(LOGIN_UNSUCCESSFUL)) {
            return false;
        } else {
            throw new EcommerceApiException("Login API message is unrecognized: " + msg);
        }
    }




}
