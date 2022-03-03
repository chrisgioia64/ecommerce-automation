package ecommerce.scenarios.product;

import ecommerce.api.JsonExtractor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class ProductUtils {

    /**
     * Extract from the json response of API 1 (Get all products list)
     * and convert to a "ProductList" java object to be used for front-end testing
     */
    public static ProductList extractProductList(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        JSONArray productArray = obj.getJSONArray("products");
        List<ProductInformation> productList = new LinkedList<>();
        for (int i = 0; i < productArray.length(); i++) {
            JSONObject productObject = productArray.getJSONObject(i);
            productList.add(extractProduct(productObject));
        }
        return new ProductList(productList);
    }

    /**
     * @param productObject
     * @return
     */
    private static ProductInformation extractProduct(JSONObject productObject) {
        ProductInformation result = new ProductInformation();
        result.setId(productObject.getInt("id"));
        result.setName(productObject.getString("name"));
        result.setPrice(extractPrice(productObject.getString("price")));
        result.setBrand(productObject.getString("brand"));
        JSONObject categoryObj = productObject.getJSONObject("category");
        String outerCategory = categoryObj.getJSONObject("usertype").getString("usertype");
        String innerCategory = categoryObj.getString("category");
        result.setOuterCategory(outerCategory);
        result.setInnerCategory(innerCategory);
        return result;
    }

    /**
     * Helper method to extract from the "price" key-value pair of the json string
     * in API 1 (Get all products)
     */
    private static int extractPrice(String priceString) {
        String[] arr = priceString.split("\\s+");
        if (arr.length == 2) {
            try {
                Integer i = Integer.parseInt(arr[1]);
                return i;
            } catch (NumberFormatException e) {
                throw new JSONException("could not parse price string : " + priceString);
            }
        }
        throw new JSONException("could not parse price string : " + priceString);
    }

    /**
     * Extract from the json response body of API 3 -- Get all brands
     */
    public static Map<Integer, String> getIdToBrandMap(String jsonResponse) {
        Map<Integer, String> map = new LinkedHashMap<>();
        JSONArray array = new JSONObject(jsonResponse).getJSONArray("brands");
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            Integer id = item.getInt("id");
            String brand = item.getString("brand");
            map.put(id, brand);
        }
        return map;
    }

    /**
     * Extract the product names from the json response of API 5 (Search Product)
     */
    public static Set<String> getProductNamesFromSearch(String jsonResponse) {
        JSONArray arr = new JSONObject(jsonResponse).getJSONArray("products");
        Set<String> productNames = new HashSet<>();
        for (int i = 0; i < arr.length(); i++) {
            JSONObject item = arr.getJSONObject(i);
            productNames.add(item.getString("name"));
        }
        return productNames;
    }

}
