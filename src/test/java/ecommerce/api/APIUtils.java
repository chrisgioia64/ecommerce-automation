package ecommerce.api;

import ecommerce.scenarios.product.ProductList;
import ecommerce.scenarios.product.ProductUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.json.JSONException;

import static io.restassured.RestAssured.given;

/**
 * Utility methods for performing API requests
 */
public class APIUtils {

    public static final Logger LOGGER = Logger.getLogger(APIUtils.class);

    /**
     * Returns the response object from calling verify login
     */
    private static Response getResponseVerifyLogin(String email, String password) {
        Response response = given().relaxedHTTPSValidation()
                .contentType(ContentType.URLENC)
                .formParam("email", email)
                .formParam("password", password)
                .when()
                .post("api/verifyLogin")
                .then()
                .extract().response();
        return response;
    }

    /**
     * API 1 : Get all products list
     */
    public static Response getResponseProductList() {
        Response response = given().relaxedHTTPSValidation()
                .when()
                .get("api/productsList")
                .then()
                .extract().response();
        return response;
    }

    /**
     * API 2 -- Post to all products list
     */
    public static Response postResponseProductList() {
        Response response = given().relaxedHTTPSValidation()
                .when()
                .post("api/productsList")
                .then()
                .extract().response();
        return response;
    }

    /**
     * API 3 - Get all brands list
     */
    public static Response getResponseBrandList() {
        Response response = given().relaxedHTTPSValidation()
                .when()
                .get("api/brandsList")
                .then()
                .extract().response();
        return response;
    }

    /**
     * API 4 - Put to all brands
     */
    public static Response putResponseBrandList() {
        Response response = given().relaxedHTTPSValidation()
                .when()
                .put("api/brandsList")
                .then()
                .extract().response();
        return response;
    }

    public static <T> T extractJson(Response response, JsonExtractor<T> extractor) throws EcommerceApiException {
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
