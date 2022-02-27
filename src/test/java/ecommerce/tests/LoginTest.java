package ecommerce.tests;

import ecommerce.base.BaseTest;
import ecommerce.features.registration.LoginTestCase;
import ecommerce.features.registration.RegistrationExcelReader;
import ecommerce.features.registration.RegistrationSpreadsheet;
import ecommerce.pageObjects.HomePage;
import ecommerce.pageObjects.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

import static org.testng.AssertJUnit.fail;

/**
 * Data-driven testing of login test cases.
 * See "login test cases" sheet of Registration excel spreadsheet specified by
 * environment file.
 */
public class LoginTest extends BaseTest {

    @Test(dataProvider = "loginTestCases", dependsOnGroups = {"registration"})
    public void test(LoginTestCase testCase) {
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
        RegistrationSpreadsheet spreadsheet = RegistrationExcelReader.getSpreadsheet();
        List<LoginTestCase> testCases = new LinkedList<>();
        for (LoginTestCase testCase : spreadsheet.getLoginTestCases().values()) {
            if (testCase.isIncludes()) {
                testCases.add(testCase);
            }
        }
        System.out.println("------------------");
        System.out.println(testCases.size());
        int size = testCases.size();
        Object[][] result = new Object[size][1];
        int index = 0;
        for (LoginTestCase testCase : testCases) {
            result[index][0] = testCase;
            index++;
        }
        return result;
    }
}
