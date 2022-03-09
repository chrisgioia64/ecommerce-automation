package ecommerce.reports;

import com.aventstack.extentreports.Status;
import ecommerce.base.BrowserType;
import ecommerce.base.DriverFactory;
import ecommerce.base.EnvironmentProperties;
import ecommerce.reports.screenshot.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class ExtentTestListener implements ITestListener {

    private final static Logger LOGGER = LogManager.getLogger(ExtentTestListener.class);

    public void onFinish(ITestContext context) {
        ExtentTestManager.endTest();
        ExtentManager.getInstance().flush();
    }

    public void onTestStart(ITestResult result) {
        ExtentTestManager.startTest(result);
    }

    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test passed");
    }

    public void onTestFailure(ITestResult result) {
        ExtentTestManager.getTest().log(Status.FAIL, "Test Failed");
        ExtentTestManager.getTest().fail(result.getThrowable());
        WebDriver driver = getDriver(result);
        ScreenshotUtils.takeScreenshot(result, driver);
    }

    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
    }

    /**
     * If we pass in the browser type as a parameter to the test method,
     * use that browser type; else use the default browser type
     */
    private WebDriver getDriver(ITestResult result) {
        BrowserType type = null;
        for (Object obj : result.getParameters()) {
            if (obj instanceof BrowserType) {
                type = (BrowserType) obj;
            }
        }
        if (type == null) {
            type = EnvironmentProperties.getInstance().getBrowserType();
        }
        return DriverFactory.getInstance().getDriver(type);
    }
}
