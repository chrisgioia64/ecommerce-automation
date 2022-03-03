package ecommerce.scenarios.registration;

import java.util.Map;

/**
 * A Java object representation of the registration excel spreadsheet
 */
public class RegistrationSpreadsheet {

    private Map<Integer, RegisteredUser> registeredUserMap;
    private Map<Integer, RegistrationCase> testCase;
    private Map<Integer, LoginCase> loginTestCases;

    public Map<Integer, RegisteredUser> getRegisteredUserMap() {
        return registeredUserMap;
    }

    public void setRegisteredUserMap(Map<Integer, RegisteredUser> registeredUserMap) {
        this.registeredUserMap = registeredUserMap;
    }

    public Map<Integer, RegistrationCase> getTestCase() {
        return testCase;
    }

    public void setTestCase(Map<Integer, RegistrationCase> testCase) {
        this.testCase = testCase;
    }

    public Map<Integer, LoginCase> getLoginTestCases() {
        return loginTestCases;
    }

    public void setLoginTestCases(Map<Integer, LoginCase> loginTestCases) {
        this.loginTestCases = loginTestCases;
    }
}
