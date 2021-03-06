package ecommerce.tests;

import ecommerce.base.BaseTest;
import ecommerce.api.APIUtils;
import ecommerce.api.EcommerceApiException;
import ecommerce.scenarios.registration.AccountExcelReader;
import ecommerce.scenarios.registration.LoginCase;
import ecommerce.scenarios.registration.AccountSpreadsheet;
import ecommerce.pages.HomePage;
import ecommerce.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import static io.restassured.RestAssured.given;
import java.util.LinkedList;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;

/**
 * Data-driven testing of login test cases.
 * See "login test cases" sheet of Registration excel spreadsheet specified by
 * environment file.
 */
public class LoginTest extends BaseTest {

    /**
     * Test login via API (API's 7 and 10)
     * @param testCase information about the login test case including
     *                 email, password, and is login successful
     */
    @Test(dataProvider = "loginTestCases",
            dependsOnGroups = {TestGroups.REGISTRATION},
            groups = {TestGroups.LOGIN, TestGroups.API})
    public void APITest(LoginCase testCase) {
        try {
            boolean successful = APIUtils.loginSuccessful(testCase.getEmail(), testCase.getPassword());
            assertEquals(testCase.getComments(), successful, testCase.isSuccessful());
        } catch (EcommerceApiException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test login via Selenium (UI)
     * @param testCase information about the login test case including
     *                 email, password, and is login successful
     */
    @Test(dataProvider = "loginTestCases",
            dependsOnGroups = {TestGroups.REGISTRATION},
            groups = {TestGroups.LOGIN, TestGroups.FRONTEND}
            )
    public void UITest(LoginCase testCase) {
        WebDriver driver = getDriver();

        HomePage homePage = new HomePage(driver);
        homePage.clickSignupLink();

        LoginPage loginPage = new LoginPage(driver);
        boolean actualSuccess = loginPage.enterLogin(testCase.getEmail(), testCase.getPassword());
        boolean expectedSuccess = testCase.isSuccessful();

        if (actualSuccess) {
            loginPage.clickLogoutLink();
            if (!expectedSuccess) {
                fail("We logged in successfully when we should not have. Reason "
                        + " " + testCase.getComments());
            }
        } else {
            if (expectedSuccess) {
                fail("We were not able to login even though we should have. Reason "
                  + testCase.getComments());
            }
        }

    }

    @DataProvider(name = "loginTestCases", parallel = true)
    public Object[][] getData() {
        AccountSpreadsheet spreadsheet = AccountExcelReader.getSpreadsheet();
        List<LoginCase> testCases = new LinkedList<>();
        for (LoginCase testCase : spreadsheet.getLoginTestCases().values()) {
            if (testCase.isIncludes()) {
                testCases.add(testCase);
            }
        }
        int size = testCases.size();
        Object[][] result = new Object[size][1];
        int index = 0;
        for (LoginCase testCase : testCases) {
            result[index][0] = testCase;
            index++;
        }
        return result;
    }
}
