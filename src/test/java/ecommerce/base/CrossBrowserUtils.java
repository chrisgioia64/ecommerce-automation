package ecommerce.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CrossBrowserUtils {

    private static Object[][] enumerate(Object[][] data, List<BrowserType> browserTypes) {
        int numBrowsers = browserTypes.size();
        Object[][] result = new Object[data.length * numBrowsers][data[0].length+1];
        for (int i = 0; i < data.length; i++) {
            int offset = i * numBrowsers;
            int index = 0;
            for (BrowserType browserType : browserTypes) {
                for (int j = 0; j < data[0].length; j++) {
                    result[offset+index][j] = data[i][j];
                }
                result[offset+index][data[0].length] = browserType;
                index++;
            }
        }
        return result;
    }

    public static Object[][] enumerateDefaultBrowser(Object[][] data) {
        return enumerate(data,
                Collections.singletonList(EnvironmentProperties.getInstance().getBrowserType()));
    }

    public static Object[][] enumerateOverAllBrowsers(Object[][] data) {
        return enumerate(data,
                Arrays.asList(BrowserType.values()));
    }

}
