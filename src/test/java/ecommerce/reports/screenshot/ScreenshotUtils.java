package ecommerce.reports.screenshot;

import com.aventstack.extentreports.MediaEntityBuilder;
import ecommerce.reports.ExtentTestManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private final static Logger LOGGER = LogManager.getLogger(ScreenshotUtils.class);


    public static void takeScreenshot(ITestResult result, WebDriver driver) {
        ITestContext context = result.getTestContext();

        String targetLocation = null;

        String testClassName = result.getInstanceName();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_kk_mm_ss_SSS");

        String timeStamp = now.format(formatter);
        String testMethodName = result.getName().toString().trim();
        String screenShotName = testMethodName + timeStamp + ".png";
        String fileSeperator = System.getProperty("file.separator");
        String reportsPath = System.getProperty("user.dir") + fileSeperator + "TestReport" + fileSeperator
                + "screenshots";
        LOGGER.info("Screen shots reports path - " + reportsPath);
        try {
            File file = new File(reportsPath + fileSeperator + testClassName); // Set
            if (!file.exists()) {
                if (file.mkdirs()) {
                    LOGGER.info("Directory: " + file.getAbsolutePath() + " is created!");
                } else {
                    LOGGER.info("Failed to create directory: " + file.getAbsolutePath());
                }

            }

            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            targetLocation = reportsPath + fileSeperator + testClassName + fileSeperator + screenShotName;// define
            // location
            File targetFile = new File(targetLocation);
            LOGGER.info("Screen shot file location - " + screenshotFile.getAbsolutePath());
            LOGGER.info("Target File location - " + targetFile.getAbsolutePath());
            FileHandler.copy(screenshotFile, targetFile);

        } catch (FileNotFoundException e) {
            LOGGER.info("File not found exception occurred while taking screenshot " + e.getMessage());
        } catch (Exception e) {
            LOGGER.info("An exception occurred while taking screenshot " + e.getCause());
        }

        // attach screenshots to report
        try {
            ExtentTestManager.getTest().fail("Screenshot",
                    MediaEntityBuilder.createScreenCaptureFromPath(targetLocation).build());
        } catch (IOException e) {
            LOGGER.info("An exception occured while taking screenshot " + e.getCause());
        }
//        ExtentTestManager.getTest().log(Status.FAIL, "Test Failed");
    }

}
