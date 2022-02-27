package ecommerce.features.registration;

import java.util.Map;

/**
 * A Java object representation of the registration excel spreadsheet
 */
public class RegistrationSpreadsheet {

    private Map<Integer, RegisteredUser> registeredUserMap;
    private Map<Integer, RegistrationTestCase> testCase;

    public Map<Integer, RegisteredUser> getRegisteredUserMap() {
        return registeredUserMap;
    }

    public void setRegisteredUserMap(Map<Integer, RegisteredUser> registeredUserMap) {
        this.registeredUserMap = registeredUserMap;
    }

    public Map<Integer, RegistrationTestCase> getTestCase() {
        return testCase;
    }

    public void setTestCase(Map<Integer, RegistrationTestCase> testCase) {
        this.testCase = testCase;
    }

}
