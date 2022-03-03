package ecommerce.api;

import io.restassured.response.Response;
import org.json.JSONException;

/**
 * Function that extracts an object T from a json string from an API response
 * @param <T>
 */
@FunctionalInterface
public interface JsonExtractor<T> {

    T extract(String json) throws JSONException;

}
