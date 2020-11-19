package com.hepsiburada.utilities.helper;


import com.hepsiburada.utilities.base.BaseTest;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Browser {

    private final Logger logger = Logger.getLogger(Browser.class);
    private DesiredCapabilities capabilities;


    private void chromeOptions(){
        ChromeOptions options = new ChromeOptions();



        capabilities = DesiredCapabilities.chrome();

        options.addArguments("--test-type");
        options.addArguments("--disable-popup-blocking");

       options.addArguments("--disable-headless");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.setExperimentalOption("useAutomationExtension", true);

        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setBrowserName(options.getBrowserName().toUpperCase());
        capabilities.setVersion(System.getProperty("os.version"));
        capabilities.setPlatform(Platform.getCurrent());
        capabilities.setCapability("acceptSslCerts", "true");
    }

    private void commonMethod(){
        BaseTest.getDriver().manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);

        if (capabilities.getPlatform().toString().equals("MAC")){
            BaseTest.getDriver().manage().window().setSize(new Dimension(1280, 900));
        }else {
            BaseTest.getDriver().manage().window().maximize();
        }

        logger.info("Installation Complete");
        logger.info("********* BROWSER:" + capabilities.getBrowserName() + ", " + "VERSION:" + capabilities.getVersion()
                + ", " + "PLATFORM:" + capabilities.getPlatform());
    }
    private  void firefoxOptions()
    {

        FirefoxOptions firefoxOptions = new FirefoxOptions();



    }

    public void createLocalDriver(String whichDriver){
        chromeOptions();
        firefoxOptions();

        if(whichDriver.equalsIgnoreCase("Firefox")) {
            System.setProperty("webdriver.gecko.driver","properties/driver/geckodriver.exe");
            BaseTest.setDriver(new FirefoxDriver());

        }
        else if(whichDriver.equalsIgnoreCase("Chrome"))
        { selectPath(capabilities.getPlatform());

            BaseTest.setDriver(new ChromeDriver(capabilities));
            commonMethod();
        }
    }

    private void selectPath(Platform platform) {
        String browser;
        if ("CHROME".equalsIgnoreCase(capabilities.getBrowserName())) {
            browser = "webdriver.chrome.driver";
            switch (platform) {
                case MAC:
                    System.setProperty(browser, "properties/driver/chromedrivermac");
                    break;
                case WIN10:
                case WIN8:
                case WIN8_1:
                case WINDOWS:
                case VISTA:
                    System.setProperty(browser, "properties/driver/chromedriver.exe");
                    break;
                case LINUX:
                    System.setProperty(browser, "properties/driver/chromedriverlinux64.exe");
                    break;
                default:
                    logger.info("PLATFORM DOES NOT EXISTS");
                    break;
            }
        }
    }
}
