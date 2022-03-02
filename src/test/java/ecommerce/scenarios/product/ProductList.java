package ecommerce.scenarios.product;

import java.util.List;

public class ProductList {

    public List<ProductInformation> productList;

    public ProductList(List<ProductInformation> productList) {
        this.productList = productList;
    }

    public List<ProductInformation> getProductList() {
        return productList;
    }
}
