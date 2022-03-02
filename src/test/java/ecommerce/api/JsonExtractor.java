package ecommerce.api;

import io.restassured.response.Response;
import org.json.JSONException;

@FunctionalInterface
public interface JsonExtractor<T> {

    public T extract(String json) throws JSONException;

}
