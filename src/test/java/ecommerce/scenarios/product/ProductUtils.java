package ecommerce.scenarios.product;

import ecommerce.api.JsonExtractor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class ProductUtils {

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

}
