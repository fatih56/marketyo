package com.hepsiburada.utilities.base;




import com.hepsiburada.utilities.helper.Browser;
import com.hepsiburada.utilities.helper.Configuration;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BaseTest {

    protected static WebDriver driver;
    private Configuration conf = new Configuration();
    private static final Logger log = Logger.getLogger(BaseTest.class);
    private static Browser browser = new Browser();

    @BeforeScenario
    public void setUp() {
        PropertyConfigurator.configure("properties/log4j.properties");
        log.info("Settings Installation Start!");

            /** LOCAL ENV **/
        System.out.println("BROWSER********** "+conf.getBrowser()+" *************************");
            browser.createLocalDriver(conf.getBrowser());
    }

    @AfterScenario
    public void tearDown()  {


        driver.manage().timeouts().pageLoadTimeout(5, SECONDS);

        //getDriver().quit();
        //log.info("Driver close");
    }



    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(RemoteWebDriver driver) {
        BaseTest.driver = driver;
    }

}