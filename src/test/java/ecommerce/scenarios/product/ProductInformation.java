package ecommerce.scenarios.product;

/**
 * Information about a single product.
 * Usually this information is obtained from the productsList API
 */
public class ProductInformation {

    private int id;
    private String name;
    private int price;
    private String brand;
    private String outerCategory;
    private String innerCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOuterCategory() {
        return outerCategory;
    }

    public void setOuterCategory(String outerCategory) {
        this.outerCategory = outerCategory;
    }

    public String getInnerCategory() {
        return innerCategory;
    }

    public void setInnerCategory(String innerCategory) {
        this.innerCategory = innerCategory;
    }

    @Override
    public String toString() {
        return id + " : " + name;
    }

}
