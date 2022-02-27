package ecommerce.base;

public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    IE("ie");

    private final String name;
    BrowserType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}