package ecommerce.tests;

import ecommerce.base.BaseTest;
import ecommerce.demo.DemoTest;
import ecommerce.features.registration.RegisteredUser;
import ecommerce.features.registration.RegistrationExcelReader;
import ecommerce.features.registration.RegistrationSpreadsheet;
import ecommerce.features.registration.RegistrationTestCase;
import ecommerce.pageObjects.HomePage;
import ecommerce.pageObjects.LoginPage;
import ecommerce.pageObjects.SignupPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.fail;

public class RegistrationTest extends BaseTest {

    private static final Logger LOGGER = Logger.getLogger(RegistrationTest.class);

    @Test(dataProvider = "registrationData", groups = {"registration"})
    public void test(RegistrationTestCase testCase, RegisteredUser user) {
        WebDriver driver = getDriver();

        HomePage homePage = new HomePage(driver);
        homePage.clickSignupLink();

        LoginPage loginPage = new LoginPage(driver);
        if (!loginPage.enterRegistration(user.getName(), user.getEmail())) {
            fail("Could not navigate to Create Account page. Most likely email " +
                    "\"" + user.getEmail() + "\" already exists");
        }

        SignupPage signupPage = new SignupPage(driver);
        signupPage.enterRegistration(user);
        signupPage.submit();

        if (signupPage.onAccountSuccessPage()) {
            LOGGER.info("User account successfully created");
        }
        if (testCase.isSuccessful()) {
            if (!signupPage.onAccountSuccessPage()) {
                fail("The account should have been created but was not created");
            } else {
                homePage.clickHomeLink();
                homePage.clickLogoutLink();
            }
        } else {
            if (signupPage.onAccountSuccessPage()) {
                homePage.clickHomeLink();
                homePage.clickLogoutLink();
                fail("The account should not have been created but was created. Reason "
                        + " " + testCase.getExplanation());
            }
        }
    }

    @DataProvider(name = "registrationData", parallel = true)
    public Object[][] getRegistrationData() {
        RegistrationSpreadsheet spreadsheet = RegistrationExcelReader.getSpreadsheet();
        List<RegistrationTestCase> testCases = new ArrayList<>();
        List<RegisteredUser> users = new ArrayList<>();
        for (RegistrationTestCase testCase : spreadsheet.getTestCase().values()) {
            RegisteredUser user = spreadsheet.getRegisteredUserMap().get(testCase.getUserId());
            if (user != null && testCase.isIncludes()) {
                testCases.add(testCase);
                users.add(user);
            } else {
                LOGGER.warn("Test case id " + testCase.getTestCaseId() + " " +
                        "has a user id " + testCase.getUserId() + " which does not exist " +
                        "in the spreadsheet");
            }
        }
        Object[][] result = new Object[testCases.size()][2];
        for (int i = 0; i < testCases.size(); i++) {
            result[i][0] = testCases.get(i);
            result[i][1] = users.get(i);
        }
        return result;
    }

}
