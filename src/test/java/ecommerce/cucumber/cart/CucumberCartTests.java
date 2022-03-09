package ecommerce.cucumber.cart;

import ecommerce.base.BaseTest;
import ecommerce.tests.TestGroups;
import io.cucumber.testng.*;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

/**
 * Disallow each of the scenarios from being ran in parallel because the
 * user account is shared across scenarios
 *
 * Copying the code from "AbstractTestNGCucumberTests" and only
 * modifying the test method. Ideally, if I understood how dependency injection
 * worked, I would create a MyCucumberTest that contained this code,
 * and then have this class extend from it, and override the necessary code.
 */
@CucumberOptions(
        features = "src/test/java/ecommerce/cucumber/cart/features",
        glue = "ecommerce.cucumber.cart.steps"
)
public class CucumberCartTests extends BaseTest {

    protected TestNGCucumberRunner testNGCucumberRunner;

    @BeforeClass(alwaysRun = true)
    public void setUpClass(ITestContext context) {
        XmlTest currentXmlTest = context.getCurrentXmlTest();
        CucumberPropertiesProvider properties = currentXmlTest::getParameter;
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass(), properties);
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (testNGCucumberRunner == null) {
            return;
        }
        testNGCucumberRunner.finish();
    }


    @SuppressWarnings("unused")
    @Test(groups = TestGroups.CART,
            description = "Cart checkout scenario",
            dataProvider = "scenarios")
    public void runScenario(PickleWrapper pickleWrapper, FeatureWrapper featureWrapper) {
        // the 'featureWrapper' parameter solely exists to display the feature
        // file in a test report
        testNGCucumberRunner.runScenario(pickleWrapper.getPickle());
    }

    /**
     * Returns two dimensional array of {@link PickleWrapper}s with their
     * associated {@link FeatureWrapper}s.
     *
     * @return a two dimensional array of scenarios features.
     */
    @DataProvider
    public Object[][] scenarios() {
        if (testNGCucumberRunner == null) {
            return new Object[0][0];
        }
        return testNGCucumberRunner.provideScenarios();
    }

}