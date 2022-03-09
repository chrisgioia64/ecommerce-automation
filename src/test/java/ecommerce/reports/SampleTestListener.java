package ecommerce.reports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Arrays;


public class SampleTestListener implements ITestListener {

    private final static Logger LOGGER = LogManager.getLogger(SampleTestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.info("Test success: {}.{}({}) ",
                result.getInstanceName(), result.getMethod().getMethodName(),
                Arrays.toString(result.getParameters()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOGGER.info("Test success: {}.{}({}) ",
            result.getInstanceName(), result.getMethod().getMethodName(),
                Arrays.toString(result.getParameters()));
    }

    private static String getUniqueTestCase(ITestResult result) {
        return result.getInstanceName() + "." + result.getMethod().getMethodName() + "(" +
                Arrays.toString(result.getParameters()) + ")";
    }
}
