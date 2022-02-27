package ecommerce.demo;

import ecommerce.base.BaseTest;
import ecommerce.base.DriverFactory;
import ecommerce.base.EnvironmentProperties;
import ecommerce.features.registration.RegisteredUser;
import ecommerce.features.registration.RegistrationExcelReader;
import ecommerce.features.registration.RegistrationSpreadsheet;
import ecommerce.features.registration.RegistrationTestCase;
import ecommerce.pageObjects.HomePage;
import ecommerce.pageObjects.LoginPage;
import ecommerce.pageObjects.SignupPage;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.testng.AssertJUnit.fail;

public class DemoTest extends BaseTest {

    private static final Logger LOGGER = Logger.getLogger(DemoTest.class);

    public static void main(String[] args) {
        EnvironmentProperties prop = EnvironmentProperties.getInstance();
        WebDriver driver = DriverFactory.getInstance().getDriver(prop.getBrowserType());
        driver.get(prop.getUrl());
    }

    @Test
    public void test() {
        WebDriver driver = getDriver();
        HomePage homePage = new HomePage(driver);
        homePage.clickSignupLink();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterRegistration("Tom Sawyer", "tomsawyer@gmail.com");
    }

    @Test(dataProvider = "registeredUsers")
    public void testDataProvider(RegisteredUser user) {
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
    }

    @DataProvider(name = "registeredUsers", parallel = true)
    public Object[][] getRegisteredUsers() {
        RegistrationSpreadsheet spreadsheet = RegistrationExcelReader.getSpreadsheet();
        int size = spreadsheet.getRegisteredUserMap().size();
        Object[][] result = new Object[size][1];
        int count = 0;
        for (Integer id : spreadsheet.getRegisteredUserMap().keySet()) {
            result[count][0] = spreadsheet.getRegisteredUserMap().get(id);
            count++;
        }
        return result;
    }

    @Test(dataProvider = "registrationData")
    public void test2(RegistrationTestCase testCase, RegisteredUser user) {
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

        if (testCase.isSuccessful()) {
            if (!signupPage.onAccountSuccessPage()) {
                signupPage.clickLogoutLink();
                fail("The account should have been created but was not created");
            } else {
                signupPage.clickLogoutLink();
            }
        } else {
            if (signupPage.onAccountSuccessPage()) {
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
            if (user != null) {
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
