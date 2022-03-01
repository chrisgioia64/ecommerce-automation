package ecommerce.features;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

import static io.restassured.RestAssured.given;

/**
 * Utility methods for performing API requests
 */
public class APIUtils {

    public static final Logger LOGGER = Logger.getLogger(APIUtils.class);

    /**
     * Returns the response object from calling verify login
     */
    private static Response verifyLogin(String email, String password) {
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
     * Performs an API request on the verify login API
     */
    public static boolean loginSuccessful(String email, String password) throws EcommerceApiException {
        Response response = verifyLogin(email, password);
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

    private static final String LOGIN_SUCCESS = "User exists!";
    private static final String LOGIN_UNSUCCESSFUL = "User not found!";


    public static void main(String[] args) throws EcommerceApiException {
        RestAssured.baseURI = "https://www.automationexercise.com/";
        Response r = verifyLogin("tomsawyer@gmail.com", "abcd123");

        System.out.println(loginSuccessful("tomsawyer@gmail.com", "abcd123"));
        System.out.println(loginSuccessful("tomsawyer@gmail.com", "abcd1234"));
    }
}
