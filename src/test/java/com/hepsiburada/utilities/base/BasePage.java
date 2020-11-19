package com.hepsiburada.utilities.base;


import com.hepsiburada.utilities.helper.Configuration;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasePage {

    public Configuration config;
    public WebDriver driver;
    private final Logger log = Logger.getLogger(BasePage.class);
    private WebDriverWait webDriverWait;
    private static String space = " ";
    private static String non_space = "";
    private String winHandleBefore = null;
    private final static int WAIT_TIME = 30;
    private static int SLEEPINMILLIS = 500;

    public BasePage(WebDriver driver) {
        this.config = Configuration.getInstance();
        this.driver = driver;
        this.webDriverWait = new WebDriverWait(driver, WAIT_TIME, SLEEPINMILLIS);
        webDriverWait.ignoring(StaleElementReferenceException.class);
    }

    public void keyboardEscape() {
        threadSleep(500);
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).perform();
        threadSleep(500);
    }

    public void keyboardEnter() {
        threadSleep(500);
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ENTER).perform();
        threadSleep(500);
    }

    public void threadSleep(int time){
        //logger("Beklenen sure : " + time + " milliseconds");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fillInputField(By by, String text, int... index) {
        fillInputField(by, text, false, index);
    }

    public void fillInputField(By by, String text, boolean pressEnter, int... index) {
        WebElement element;
        element = findElementIfExist(by, "5", index);
            try {

              //  element.click();
                element.clear();
                element.sendKeys(text);
                if (pressEnter) {
                    element.sendKeys(Keys.ENTER);
                }
                  } catch (Exception e)
            {
                e.printStackTrace(); }

    }

    public void fillInputFieldSlow(By by, String text, boolean pressEnter, int... index) {
        WebElement element;
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();
        boolean flag = false;
        System.out.println("Elementin bulunma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        while (isOperationInTimeout(finishTime, timeLeftSeconds(startTime, finishTime, WAIT_TIME))) {
            try {
                if (element.isEnabled()) {
                    element.clear();
                    element.sendKeys(text);
                    threadSleep(1000);
                    if (pressEnter) {
                        element.sendKeys(Keys.ENTER);
                    }
                    flag = true;
                    break;
                }
            } catch (Exception e) {
                element = findElement(by, index);
            }
            threadSleep(SLEEPINMILLIS);
        }
        if(flag == false){
            Assert.fail("Alan doldurulurken hata ile karsilasildi.. locator : " + by + " text : " + text);
        }
    }

    public void fillInputFieldByScrollCenter(By by, String text, boolean pressEnter, int... index) {
        WebElement element = findElement(by, index);
        for(int i = 0; i < 4; i++) {
            try {
                if (element.isEnabled()) {
                    element.clear();
                    element.sendKeys(text);
                    if (pressEnter) {
                        element.sendKeys(Keys.ENTER);
                    }
                    break;
                }
            }
            //catch (NullPointerException ex) {
            //    if(i == 3)
            //        Assert.fail("Nullpointer Exception for:" + by);
            //}
            catch (Exception e) {

                if(i == 3)
                    Assert.fail("/n Alan doldurulurken hata ile karsilasildi.. locator : " + by + "text : " + text);
            }
            System.out.println("Girilcek texti " + text + " olan eleman " + (i + 1) + ". kez arandi...");
            threadSleep(3000);
            scrollToCenter(driver.findElement(by));
            driver.findElement(by).click();
        }
    }

    public WebElement findElementTest(By by, int... index) {

        WebElement element = null;
        WebDriverWait w = new WebDriverWait(driver, 2, SLEEPINMILLIS);
        long startTime = System.currentTimeMillis();

            if (index.length == 0) {
                try {
                    for(int i = 0; i < 2; i++) {
                        try {
                            element = w.until(ExpectedConditions.presenceOfElementLocated(by));
                            break;
                        }
                        catch (Exception e) {
                            if(i == 1)
                                Assert.fail("Element bulunurken hata ile karsilasildi.. " + (i + 1) + ". deneme elemet locator : " + by);
                            System.out.println("Element bulunurken hata ile karsilasildi.. " + (i + 1) + ". deneme elemet locator : " + by);
                        }
                        threadSleep(1000);
                    }
                } catch (Exception e) {
                    return null;
                }
            } else if (index[0] >= 0) {
                try {
                    for (int i = 0; i < 2; i++) {
                        try {
                            element = w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
                            break;
                        }
                        //catch (StaleElementReferenceException e) {
                        //    if(i == 1)
                        //        Assert.fail("**********--------- Stale Referance Exeption : " + (i + 1) + ". deneme elemet locator : " + by);
                        //    System.out.println("**********--------- Stale Referance Exeption : " + (i + 1) + ". deneme elemet locator : " + by);
                        //    }
                        catch (Exception e) {
                            if(i == 1)
                                Assert.fail("Element bulunurken hata ile karsilasildi.. " + (i + 1) + ". deneme elemet locator : " + by);
                            System.out.println("Element bulunurken hata ile karsilasildi.. " + (i + 1) + ". deneme elemet locator : " + by);
                        }
                        threadSleep(1000);
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        return element;
    }

    public WebElement findElement(By by, int... index) {
        WebElement element = null;
        WebDriverWait w = new WebDriverWait(driver, 2, SLEEPINMILLIS);
        long startTime = System.currentTimeMillis();
        boolean flag = false;
        if (index.length == 0) {
            while (isOperationInTimeout(startTime, WAIT_TIME)) {
                try {
                    element = driver.findElement(by);
                            //w.until(ExpectedConditions.presenceOfElementLocated(by));
                    flag = true;
                    break;
                } catch (Exception e) { }
                threadSleep(SLEEPINMILLIS);
            }
        } else if (index[0] >= 0) {
            while (isOperationInTimeout(startTime, WAIT_TIME)) {
                try {
                    //w.until(ExpectedConditions.presenceOfElementLocated(by));
                    //threadSleep(500);
                    element = driver.findElements(by).get(index[0]);
                            //w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
                    flag = true;
                    break;
                } catch (Exception e) { }
                threadSleep(SLEEPINMILLIS);
            }
        }
        if(flag == false){
            //Assert.fail("Element bulunurken hata ile karsilasildi.. elemet locator : " + by + " - timeout : " + WAIT_TIME);
            assertMessage("Element bulunurken hata ile karsilasildi.. elemet locator : " + by + " - timeout : " + WAIT_TIME);
        }
        //System.out.println("Element bulundu.. Locator : " + by + " Total time : " + (endTime - startTime) + " milliseconds");
        return element;
    }

    public WebElement findElementClickable(By by, int... index) {
        WebElement element = null;
        WebDriverWait w = new WebDriverWait(driver, 2, SLEEPINMILLIS);
        long startTime = System.currentTimeMillis();
        boolean flag = false;
        if (index.length == 0) {
            while (isOperationInTimeout(startTime, WAIT_TIME)) {
                try {
                    element = w.until(ExpectedConditions.elementToBeClickable(by));
                    flag = true;
                    break;
                } catch (Exception e) { }
                threadSleep(SLEEPINMILLIS);
            }
        } else if (index[0] >= 0) {
            while (isOperationInTimeout(startTime, WAIT_TIME)) {
                try {
                    element = w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
                    flag = true;
                    break;
                } catch (Exception e) { }
                threadSleep(SLEEPINMILLIS);
            }
        }
        if(flag == false){
            //Assert.fail("Element bulunurken hata ile karsilasildi.. elemet locator : " + by + " - timeout : " + WAIT_TIME);
            assertMessage("Element bulunurken hata ile karsilasildi.. elemet locator : " + by + " - timeout : " + WAIT_TIME);
        }
        //System.out.println("Element bulundu.. Locator : " + by + " Total time : " + (endTime - startTime) + " milliseconds");
        return element;
    }

    public WebElement findElementForSelect(By by) {
        WebElement element = null;
        WebDriverWait w = new WebDriverWait(driver, 2, SLEEPINMILLIS);
        long startTime = System.currentTimeMillis();
        //boolean flag = false;
        String locatorText = by.toString();
        locatorText = locatorText.substring(locatorText.indexOf(":") + 1).trim();
        locatorText = locatorText + "/option";
        By OPTION = By.xpath(locatorText);

        while (isOperationInTimeout(startTime, WAIT_TIME)) {
            element = w.until(ExpectedConditions.presenceOfElementLocated(by));
            if (driver.findElements(OPTION).size() >= 2)
                break;
            if (element != null)
                break;
        }

        if(element == null){
            Assert.fail("Element bulunurken hata ile karsilasildi.. elemet locator : " + by + " - timeout : " + WAIT_TIME);
        }
        /*
        while (isOperationInTimeout(startTime, WAIT_TIME)) {
            while (isOperationInTimeout(startTime, WAIT_TIME)) {
                try {
                    element = w.until(ExpectedConditions.presenceOfElementLocated(by));
                    scrollUntilDisplayed(by, "3");
                    flag = true;
                    break;
                } catch (Exception e) {
                }
                threadSleep(SLEEPINMILLIS);
            }
            threadSleep(SLEEPINMILLIS);
        if (driver.findElements(OPTION).size() >= 2)
            break;
        }
        System.out.println("Secim yapilacak selectbox size : " + driver.findElements(OPTION).size());

        if(flag == false){
            Assert.fail("Element bulunurken hata ile karsilasildi.. elemet locator : " + by + " - timeout : " + WAIT_TIME);
        }
        */
        return element;
    }

    public WebElement findElementIfExist(By by, int... index) {
        WebElement element = null;
        WebDriverWait w = new WebDriverWait(driver, 2, SLEEPINMILLIS);
        long startTime = System.currentTimeMillis();
        if (index.length == 0) {
            while (isOperationInTimeout(startTime, WAIT_TIME)) {
                try {
                    element = w.until(ExpectedConditions.presenceOfElementLocated(by));
                    break;
                } catch (Exception e) { }
                threadSleep(SLEEPINMILLIS);
            }
        } else if (index[0] >= 0) {
            while (isOperationInTimeout(startTime, WAIT_TIME)) {
                try {
                    element = w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
                    break;
                } catch (Exception e) { }
                threadSleep(SLEEPINMILLIS);
            }
        }
        //long endTime = System.currentTimeMillis();

        //System.out.println("Element bulundu.. Locator : " + by + " Total time : " + (endTime - startTime) + " milliseconds");
        return element;
        /*
        WebElement element = null;
        WebDriverWait w = new WebDriverWait(driver, 10, SLEEPINMILLIS);

        if (index.length == 0) {
            try {
                for(int i = 0; i < 2; i++) {
                    try {
                        element = w.until(ExpectedConditions.presenceOfElementLocated(by));
                        break;
                    } catch (Exception e) {
                        System.out.println("Element bulunurken hata ile karsilasildi.. " + (i + 1) + ". deneme elemet locator : " + by);
                    }
                    threadSleep(1000);
                }
            } catch (Exception e) {
                return null;
            }
        } else if (index[0] >= 0) {
            try {
                for (int i = 0; i < 2; i++) {
                    try {
                        element = w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
                        break;
                    } catch (Exception e) {
                        System.out.println("Element bulunurken hata ile karsilasildi.. " + (i + 1) + ". deneme elemet locator : " + by);
                    }
                    threadSleep(1000);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return element;*/
    }

    public WebElement findElementIfExist(By by, String stimeout, int... index) {
        int timeout = Integer.parseInt(stimeout);
        WebElement element = null;
        //WebDriverWait w = new WebDriverWait(driver, 2, SLEEPINMILLIS);
        long startTime = System.currentTimeMillis();
        if (index.length == 0) {
            while (isOperationInTimeout(startTime, timeout)) {
                try {
                    element = driver.findElement(by);
                    break;
                } catch (Exception e) { }
                threadSleep(SLEEPINMILLIS);
            }
        } else if (index[0] >= 0) {
            while (isOperationInTimeout(startTime, timeout)) {
                try {
                    element = driver.findElements(by).get(index[0]);
                    break;
                } catch (Exception e) { }
                threadSleep(SLEEPINMILLIS);
            }
        }
        return element;

        /*
        WebElement element = null;
        WebDriverWait w = new WebDriverWait(driver, Integer.parseInt(timeout) / 2, SLEEPINMILLIS);

        if (index.length == 0) {
            try {
                for(int i = 0; i < 2; i++) {
                    try {
                        element = w.until(ExpectedConditions.presenceOfElementLocated(by));
                        break;
                    } catch (Exception e) {
                        System.out.println("Element bulunurken hata ile karsilasildi.. " + (i + 1) + ". deneme elemet locator : " + by);
                    }
                    threadSleep(1000);
                }
            } catch (Exception e) {
                return null;
            }
        } else if (index[0] >= 0) {
            try {
                for (int i = 0; i < 2; i++) {
                    try {
                        element = w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
                        break;
                    } catch (Exception e) {
                        System.out.println("Element bulunurken hata ile karsilasildi.. " + (i + 1) + ". deneme elemet locator : " + by);
                    }
                    threadSleep(1000);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return element;*/
    }

    public WebElement findElement(By by, String stimeout ,int... index) {
        int timeout = Integer.parseInt(stimeout);
        WebElement element = null;
        WebDriverWait w = new WebDriverWait(driver, 2, SLEEPINMILLIS);
        long startTime = System.currentTimeMillis();
        boolean flag = false;
        if (index.length == 0) {
            while (isOperationInTimeout(startTime, timeout)) {
                try {
                    element = w.until(ExpectedConditions.presenceOfElementLocated(by));
                    flag = true;
                    break;
                } catch (Exception e) {
                    driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
                }
                threadSleep(SLEEPINMILLIS);
            }
        } else if (index[0] >= 0) {
            while (isOperationInTimeout(startTime, timeout)) {
                try {
                    element = w.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
                    flag = true;
                    break;
                } catch (Exception e) {
                    driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
                }
                threadSleep(SLEEPINMILLIS);
            }
        }
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        if(flag == false){
            //throw new AssertionError();
            Assert.fail("Element bulunurken hata ile karsilasildi.. elemet locator : " + by + " timeout : " + WAIT_TIME);
        }
        //System.out.println("Element bulundu.. Locator : " + by + " Total time : " + (endTime - startTime) + " milliseconds");
        return element;
        /*
        WebElement element = null;
        WebDriverWait wait = new WebDriverWait(driver, timeout, SLEEPINMILLIS);
        if (index.length == 0) {
            try {
                element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            } catch (Exception e) {
                return null;
            }
        } else if (index[0] >= 0) {
            try {
                element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
            } catch (Exception e) {
                return null;
            }
        }
        return element;*/
    }

    /*
    public WebElement findElement(By by, String timeout ,int... index) {
        WebElement element = null;
        WebDriverWait wait = new WebDriverWait(driver, Integer.parseInt(timeout), SLEEPINMILLIS);
        if (index.length == 0) {
            try {
                element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            } catch (Exception e) {
                return null;
            }
        } else if (index[0] >= 0) {
            try {
                element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by)).get(index[0]);
            } catch (Exception e) {
                return null;
            }
        }
        return element;
    }*/

    public String getWebElementText(By by, int... index) {
        try {
            return findElement(by, index).getText();
        }catch (Exception e){
            Assert.assertTrue("Nullpointer Exception for:" + by, false);
            return null;
        }
    }

    public int getWebElementsCount(By by, int... index) {
        return findElementList(by).size();
    }

    public List<String> getWebElementsText(By by) {
        ArrayList<String> list = new ArrayList<>();
        for (WebElement el : findElementList(by)) {
            HighlightElement(el);
            list.add(el.getText());
        }
        return list;
    }

    public String getURL(){
        return driver.getCurrentUrl();
    }

    public String removeEmptySpace(String old_string) {
        return old_string.replaceAll(space, non_space);
    }

    public String getAttribute(By by, String attributeName, int... index) {
        return findElement(by, index).getAttribute(attributeName);
    }

    public void HighlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element,
                "color: red; border: 1px dashed red;");
    }

    public void switchNewTab() {
        winHandleBefore = driver.getWindowHandle(); //ilk bulundugun pencere

        Set<String> winHandles = driver.getWindowHandles();
        for (String win : winHandles)
            driver.switchTo().window(win);
    }

    public void switchParent() {
        driver.switchTo().window(winHandleBefore);
    }

    public void closeChildWindow() {
        String winHandleBefore = driver.getWindowHandle();
        for (String handle1 : driver.getWindowHandles()) {
            System.out.println(handle1);
            driver.switchTo().window(handle1);
        }
        driver.close();
        driver.switchTo().window(winHandleBefore);
    }

    public boolean elementExist(By by, int... index) {
        try {
            WebElement el = findElement(by, index);
            return el.isDisplayed();
        } catch (NullPointerException e) {
            return false;
        }
    }

    public List<WebElement> findElementList(By by) {
        try {
            return driver.findElements(by);
        }catch (NullPointerException e){
            Assert.assertTrue("Nullpointer Exception for:" + by, false);
            return null;
        }
    }

    public void executeJSE(String js){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript(js);
    }

    public void waitUntilExpectedElementClickable(WebElement element) {
        WebDriverWait w = new WebDriverWait(driver,WAIT_TIME / 2);
        for(int i = 0; i < 2; i++) {
            try {
                w.until(ExpectedConditions.elementToBeClickable(element));
            } catch (Exception e) {
                if(i == 1) {
                    Assert.fail("Element tiklanabilir degil!!!" + element);
                    e.getMessage();
                }
            }
            threadSleep(1000);
        }
    }

    public void waitUntilExpectedElementClickableIfExist(WebElement element) {
        WebDriverWait w = new WebDriverWait(driver,WAIT_TIME / 2);
        for(int i = 0; i < 2; i++) {
            try {
                w.until(ExpectedConditions.elementToBeClickable(element));
            } catch (Exception e) { }
            threadSleep(1000);
        }
    }

    public void waitUntilExpectedElementClickable(WebElement element, int timeout) {
        WebDriverWait w = new WebDriverWait(driver, timeout/2, SLEEPINMILLIS);
        for(int i = 0; i < 2; i++) {
            try {
                //HighlightElement(element);
                w.until(ExpectedConditions.elementToBeClickable(element));
            } catch (Exception e) {
                if(i == 1)
                    Assert.fail("Beklenen element clickable olmadi.. Element : " + element);
            }
        }
    }

    public void waitUntilExpectedElementClickable(By by) {
        AssertionError assertionError = new AssertionError();

        for (int i = 0; i < 2; i++) {
            try {
                webDriverWait.until(ExpectedConditions.elementToBeClickable(by));
            }catch (Exception e) {
                if(i == 1) {
                    assertionError.getMessage();
                    Assert.fail("Elementin tiklanabilir olabilmesi icin beklenirken hata ile karsilasildi..:" + by + (i + 1) + ". deneme");
                    e.getMessage();
                }
            }
            threadSleep(1000);
        }
    }

    public void spinnerWait(By elementBy) {
        WebElement element=driver.findElement(elementBy);
        WebDriverWait wait = new WebDriverWait(driver, 200);
        wait.until(ExpectedConditions.invisibilityOf(element));

    }


    public void waitUntilExpectedElementClickable(By by, String timeOut, int... index) {
        AssertionError assertionError=new AssertionError();

        WebDriverWait w = new WebDriverWait(driver, Integer.parseInt(timeOut), SLEEPINMILLIS);
        if(index.length == 0) {
            try {
                w.until(ExpectedConditions.elementToBeClickable(by));
            } catch (Exception e) {
                Assert.fail("Element tiklanabilir degil!!! locator : " + by);
            }
        }else {
            try {
                //HighlightElement(w.until(ExpectedConditions.visibilityOf(driver.findElements(by).get(index[0]))));
                w.until(ExpectedConditions.elementToBeClickable(by));
            } catch (Exception e) {
                Assert.fail("Element tiklanabilir degil!!! locator : " + by);
                e.getMessage();
            }
        }
    }

    public void waitUntilExpectedElementClickableIfExist(WebElement element, int timeout) {
        WebDriverWait w = new WebDriverWait(driver, timeout/2, SLEEPINMILLIS);
        for(int i = 0; i < 2; i++) {
            try {
                //HighlightElement(element);
                w.until(ExpectedConditions.elementToBeClickable(element));
            } catch (Exception e) { }
            threadSleep(1000);
        }
    }

    public void waitUntilExpectedElement(By by, String timeout, int... index) {
        long startTime = System.currentTimeMillis();
        WebElement element;
        int itimeout = Integer.parseInt(timeout);
        boolean flag = false;
        while (isOperationInTimeout(startTime, itimeout)){
            try{
                element = findElementIfExist(by,"3", index);
                threadSleep(SLEEPINMILLIS);
                if(element.isDisplayed()){
                    flag = true;
                    break;
                }
            }catch (Exception e){}
        }
        if(flag == false){
            Assert.fail("Beklenen element gelmedi.. Locator : " + by + " timeout : " + itimeout);
        }
    }

    public void waitUntilExpectedElement(By by, String timeout, String assertionMessage, int... index) {
        long startTime = System.currentTimeMillis();
        int itimeout = Integer.parseInt(timeout);
        WebElement element;
        boolean flag = false;
        while (isOperationInTimeout(startTime, itimeout)){
            try{
                element = findElementIfExist(by,"3", index);
                threadSleep(SLEEPINMILLIS);
                if(element.isDisplayed()){
                    flag = true;
                    break;
                }
            }catch (Exception e){}
        }
        if(flag == false){
            Assert.fail(assertionMessage + " Locator : " + by + " timeout : " + itimeout);
        }
    }

    public void waitUntilExpectedElement(By by, int... index){
        long startTime = System.currentTimeMillis();
        WebElement element;
        boolean flag = false;
        while (isOperationInTimeout(startTime, WAIT_TIME)){
            try{
                element = findElementIfExist(by,"3", index);
                threadSleep(SLEEPINMILLIS);
                if(element.isDisplayed()){
                    flag = true;
                    break;
                }
            }catch (Exception e){}
        }
        if(flag == false){
            Assert.fail("Beklenen element gelmedi.. Locator : " + by + " timeout : " + WAIT_TIME);
        }
    }

    public boolean waitElementExistInDefaultTimeout(By by, long timeout, int... index) {
        WebElement element = findElementIfExist(by,index);
        if(index.length == 0) {
            try {
                for(int k = 0; k < 4; k++){
                    scrollToElement(element);
                    if(element.isDisplayed())
                        return true;
                    threadSleep(500);
                }
            } catch (Exception e) {
                System.out.println("Aranan obje ekranda gorulmedi.. timeout : " + timeout + " locator : " + by);
                return false;
            }
        }
        return false;
    }

    public void waitIfElementExistInDefaultTimeout(By by, int... index) {
//        WebElement element;
        WebDriverWait w = new WebDriverWait(driver, WAIT_TIME, SLEEPINMILLIS);
        for(int i = 0; i < 2; i++) {
            try {
                w.until(ExpectedConditions.presenceOfElementLocated(by));
                break;
            }catch (Exception e){ }
            threadSleep(1000);
        }
    }

    public void waitIfElementExist(By by, long seconds, String message, int... index) {

        WebDriverWait temp  = new WebDriverWait(driver, seconds / 2, SLEEPINMILLIS);
        for (int i = 0; i < 2; i++) {
            try {
                temp.until(ExpectedConditions.presenceOfElementLocated(by));
                logger(message + "Timeout(Seconds) : " + seconds + "Beklenen element locator : " + by);
                break;
            }catch (Exception e){
                logger(message + "Timeout(Seconds) : " + seconds);
            }
            threadSleep(1000);
        }
    }

    public void switchNewTabCloseLastTab(){
        ArrayList<String> tabs2 = new ArrayList<> (driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(0));
        driver.close();
        driver.switchTo().window(tabs2.get(1));
    }

    public void switchLastTabCloseNewTab(){
        ArrayList<String> tabs2 = new ArrayList<> (driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));
        driver.close();
        driver.switchTo().window(tabs2.get(0));
    }

    public String regexFirstGroup(String regexValue, String stringVariable){
        String stringVariableText = null;
        final Matcher m = Pattern.compile(regexValue).matcher(stringVariable);
        if (m.find()) {
            stringVariableText = m.group(0);
        }

        return stringVariableText;
    }

    public boolean isElementClickable(By by, int... index) {
        WebElement element;
        boolean returnStatement = false;
        try {
            element = findElement(by, index);
            if (element.isEnabled()) {
                returnStatement = true;
            }
        } catch (Exception e) {
            System.out.println("Element clickable degil!!! : " + by);
        }
        return returnStatement;
    }

    public void mapLocationSelect(By by, int... index){
        WebElement element = findElement(by, index);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).moveByOffset(30, 30).click().perform();
    }

    public void iFrameElement(By by){
        WebElement iFrameSwitch = findElement(by);
        try {
            driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
            driver.switchTo().frame(iFrameSwitch);
            System.out.println("Frame locator : " + by);
            driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        }catch (Exception e) {
            try {
                driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
                driver.switchTo().frame(iFrameSwitch);
                System.out.println("Frame locator : " + by);
                driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
            }catch (Exception ex){
                Assert.fail("Frame degistirilemedi... Locator : " + by);
                e.getMessage();
            }
        }
    }

    public boolean isButtonActive(By by, String classAttribute, int... index){
        boolean returnStatement = false;
        WebElement button = findElement(by, index);
        String getAttribute = button.getAttribute(classAttribute);

        if (getAttribute == null){
            returnStatement = true;
        }else if (getAttribute.equals("true")){
            returnStatement = false;
        }

        return returnStatement;
    }

    public void alertAccept(){
        driver.switchTo().alert().accept();
    }

    public WebElement submitObjectBy(By by, int... index) {
        webDriverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        WebElement element = findElement(by, index);
        element.submit();

        return element;
    }

    public void logger(String message) {
        if (message != null) {
            log.info(message);
        }
    }

    public void enterTextByAction(String text) {
        Actions action = new Actions(driver);
        for(int i= 0; i < 2; i++) {
            try {
                action.sendKeys(text).build().perform();
                break;
            }catch (Exception e) {
                if(i == 1) {
                    Assert.fail("Yazilacak texti (" + text + ") olan elemente text yazilirken hata ile karsilasildi...");
                    //assertMessage("Yazilacak texti (" + text + ") olan elemente text yazilirken hata ile karsilasildi...");
                    e.getMessage();
                }
            }
            threadSleep(1000);
        }
    }

    public void clickObjectIfExistWithJS(By by, String message, String stimeout, int... index) {
        int timeout = Integer.parseInt(stimeout);
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        long startTime = System.currentTimeMillis();
        element = findElementIfExist(by, "5", index);
        long finishTime = System.currentTimeMillis();
        if (element != null) {
            System.out.println("Elementin bulunma suresi : " + (finishTime - startTime) + " milliseconds Locator : " + by);
        }
        while (isOperationInTimeout(startTime, timeout)) {
            try {
                clickWithJS(element);
                logger("--------------------------- " + message);
                break;
            } catch (Exception e) {
                element = findElementIfExist(by, "2", index);
                try {
                    scrollToElementWithAction(element);
                    clickWithJS(element);
                    logger("--------------------------- " + message);
                    break;
                } catch (Exception ex) {
                    element = findElementIfExist(by, "2", index);
                    try {
                        scrollToElementWithJS(element);
                        clickWithJS(element);
                        logger("--------------------------- " + message);
                        break;
                    } catch (Exception exc) {
                        element = findElementIfExist(by, "2", index);
                        try {
                            scrollToCenter(element);
                            clickWithJS(element);
                            logger("--------------------------- " + message);
                            break;
                        } catch (Exception excp) {
                            element = findElementIfExist(by, "2", index);
                        }
                    }
                }
            }
        }
    }

    public void clickObjectIfExist(By by, String message, String stimeout, int... index) {
        int timeout = Integer.parseInt(stimeout);
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        System.out.println("Beklenen element locator : " + by);
        long startTime = System.currentTimeMillis();
        element = findElementIfExist(by,"5", index);
        long finishTime = System.currentTimeMillis();
        if(element != null){
            System.out.println("Elementin bulunma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        }
        while (isOperationInTimeout(startTime, timeout)){
            try {
                element.click();
                logger("--------------------------- " + message);
                break;
            }catch (Exception e){
                element = findElementIfExist(by,"2", index);
                try {
                    scrollToElementWithAction(element);
                    element.click();
                    logger("--------------------------- " + message);
                    break;
                }catch (Exception ex){
                    element = findElementIfExist(by,"2", index);
                    try {
                        scrollToElementWithJS(element);
                        element.click();
                        logger("--------------------------- " + message);
                        break;
                    }catch (Exception exc){
                        element = findElementIfExist(by,"2", index);
                        try {
                            scrollToCenter(element);
                            element.click();
                            logger("--------------------------- " + message);
                            break;
                        }catch (Exception excp){
                            element = findElementIfExist(by,"2", index);
                        }
                    }
                }
            }
        }

        /*
        int timeout = Integer.parseInt(stimeout);
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();
        //System.out.println("Elementin bulunma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        while (isOperationInTimeout(finishTime, timeLeftSeconds(startTime, finishTime, timeout))) {
            try {
                for (int i = 0; i < 2; i++) {
                    scrollIntoView(by);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                    scrollToVertical(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                    scrollToCenter(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                    scrollToElement(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                }
                threadSleep(300);
                element.click();
                logger("--------------------------- " + message + "         ");
                break;
            }catch (Exception e){
                element = findElement(by, index);
            }
            threadSleep(SLEEPINMILLIS);
        }
        */

        /*
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        element = findElementIfExist(by, index);
        try {
            if (element == null){
                throw new RuntimeException("ELEMENT (" + by + (index.length > 0 ? index[0] : "")
                        + ") NOT EXIST; AUTOMATION DATAS MAY BE INVALID!");
            }else {
                for(int k = 0; k < 2; k++){
                    try {
                        for (int i = 0; i < 2; i++) {
                            scrollToVertical(element);
                            if(isElementDisplayed(by))
                                break;
                            threadSleep(500);
                        }
                        waitUntilExpectedElementClickableIfExist(element, (Integer.parseInt(timeout))/2);
                        threadSleep(300);
                        element.click();
                        logger("--------------------------- " + message + "         ");
                        break;
                    }catch (Exception e){ }
                    threadSleep(500);
                    System.out.println("Tiklanicak element araniyor.. " + (k + 1) + ". deneme Locator : " + by);
                }
            }
        }catch (Exception e){ }
        */
    }

    public void clickObject(By by, String message, String stimeout, int... index) {
        int timeout = Integer.parseInt(stimeout);
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();
        boolean flag = false;
        System.out.println("Elementin bulunma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        while (isOperationInTimeout(startTime,timeout)){
            try {
                scrollToElement(element);
                element.click();
                flag = true;
                logger("--------------------------- " + message);
                break;
            }catch (Exception e){
                if(!isOperationInTimeout(startTime, WAIT_TIME))
                    break;
                element = findElement(by, "1", index);
                try{
                    scrollToElementWithAction(element);
                    element.click();
                    flag = true;
                    logger("--------------------------- " + message);
                    break;
                }catch (Exception ex){
                    if(!isOperationInTimeout(startTime, WAIT_TIME))
                        break;
                    element = findElement(by, "1", index);
                    try{
                        scrollToCenter(element);
                        element.click();
                        flag = true;
                        logger("--------------------------- " + message);
                        break;
                    }catch (Exception exp){
                        if(!isOperationInTimeout(startTime, WAIT_TIME))
                            break;
                        element = findElement(by, "1", index);
                        try {
                            scrollToIfNeeded(element);
                            element.click();
                            flag = true;
                            logger("--------------------------- " + message);
                            break;
                        }catch (Exception expc){}
                    }
                }
            }
        }
        if(flag == false){
            Assert.fail("Element tiklanamadi!!! Locator : " + by + "  - timeout : " + Integer.parseInt(stimeout));
            //assertMessage("Element tiklanamadi!!! Locator : " + by + "  - timeout : " + WAIT_TIME);
        }

        /*
        int timeout = Integer.parseInt(stimeout);
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();
        boolean flag = false;
        System.out.println("Elementin bulunma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        while (isOperationInTimeout(finishTime, timeLeftSeconds(startTime, finishTime, timeout))) {
            try {
                for (int i = 0; i < 2; i++) {
                    scrollIntoView(by);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                    scrollToCenter(element);
                    threadSleep(500);
                    if(isElementDisplayed(by))
                        break;
                    scrollToElement(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                }
                threadSleep(300);
                element.click();
                logger("--------------------------- " + message + "         ");
                flag = true;
                break;
            }catch (Exception e){
                element = findElement(by, index);
            }
            threadSleep(SLEEPINMILLIS);
        }
        if(flag == false){
            Assert.fail("Element tiklanamadi!!! Locator : " + by + "  - timeout : " + WAIT_TIME);
            //assertMessage("Element tiklanamadi!!! Locator : " + by + "  - timeout : " + WAIT_TIME);
        }*/
        /*
        //AssertionError assertionError=new AssertionError();
        element = findElement(by, index);
        try {
            if (element == null){
                throw new RuntimeException("ELEMENT (" + by + (index.length > 0 ? index[0] : "")
                        + ") NOT EXIST; AUTOMATION DATAS MAY BE INVALID!");
            }else {
                for(int k = 0; k < 2; k++){
                    try {
                        for (int i = 0; i < 2; i++) {
                            scrollToVertical(element);
                            if(isElementDisplayed(by))
                                break;
                            threadSleep(1000);
                        }
                        waitUntilExpectedElementClickable(element, (Integer.parseInt(timeout))/2);
                        threadSleep(300);
                        element.click();
                        logger("--------------------------- " + message + "         ");
                        break;
                    }catch (Exception e){
                        if(k == 1) {
                            //Assert.fail("Element clickable degil!!! Locator : " + by);
                            //e.printStackTrace();
                            assertMessage("Element clickable degil!!! Locator : " + by);
                        }
                    }
                    threadSleep(500);
                    System.out.println("Element tiklanirken hata ile karsilasildi.. " + (k + 1) + ". deneme" + by);
                }
            }
        }catch (NullPointerException e){
            Assert.fail("Element bulunamadi!!!! Locator : " + by);
            //assertMessage("Element bulunamadi!!!! Locator : " + by);
        }*/
    }

/*
    public void clickObjectTest(By by, String message, int... index) {
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();

        while (isOperationInTimeout(finishTime, timeLeftSeconds(startTime, finishTime, WAIT_TIME))) {


        }


        try {
            if (element == null){
                throw new RuntimeException("ELEMENT (" + by + (index.length > 0 ? index[0] : "")
                        + ") NOT EXIST; AUTOMATION DATAS MAY BE INVALID!");
            }else {
                waitUntilExpectedElement(by);
                for(int k = 0; k < 2; k++){
                    try {
                        for (int i = 0; i < 2; i++) {
                            scrollToVertical(element);
                            if(isElementDisplayed(by))
                                break;
                            threadSleep(300);
                        }
                        threadSleep(300);
                        element.click();
                        logger("--------------------------- " + message + "         ");
                        break;
                    }catch (Exception e){
                        if(k == 1) {
                            Assert.fail("Element tiklanamadi!! locator : " + by);
                        }
                    }
                    threadSleep(1500);
                    System.out.println("Element tiklanirken hata ile karsilasildi.. " + (k + 1) + ". deneme" + by);
                }
            }
        }catch (Exception e){
            Assert.fail("Element tiklanamadi!!! Locator : " + by);
            //assertMessage("Element tiklanamadi!!! Locator : " + by);
        }
    }
*/

    public void clickObject(By by, String message, int... index) {
        WebElement element;
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();
        boolean flag = false;
        System.out.println("Elementin aranma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        while (isOperationInTimeout(startTime, WAIT_TIME)) {
            try{
                element.click();
                flag = true;
                logger("--------------------------- " + message);
                break;
            }catch (Exception exception){
                try {
                    scrollToElement(element);
                    element.click();
                    flag = true;
                    logger("--------------------------- " + message);
                    break;
                }catch (Exception e) {
                    if (!isOperationInTimeout(startTime, WAIT_TIME))
                        break;
                    element = findElement(by, "1", index);
                    try {
                        scrollToElementWithAction(element);
                        element.click();
                        flag = true;
                        logger("--------------------------- " + message);
                        break;
                    } catch (Exception ex) {
                        if (!isOperationInTimeout(startTime, WAIT_TIME))
                            break;
                        element = findElement(by, "1", index);
                        try {
                            scrollToCenter(element);
                            element.click();
                            flag = true;
                            logger("--------------------------- " + message);
                            break;
                        } catch (Exception exp) {
                            if (!isOperationInTimeout(startTime, WAIT_TIME))
                                break;
                            element = findElement(by, "1", index);
                            try {
                                scrollToIfNeeded(element);
                                element.click();
                                flag = true;
                                logger("--------------------------- " + message);
                                break;
                            } catch (Exception expc) {
                            }
                        }
                    }
                }
            }
        }

        /*
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();
        boolean flag = false;
        System.out.println("Elementin bulunma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        while (isOperationInTimeout(finishTime, timeLeftSeconds(startTime, finishTime, WAIT_TIME))) {
            try {
                scrollUntilDisplayed(by, index);
                threadSleep(300);
                element.click();
                logger("--------------------------- " + message + "         ");
                flag = true;
                break;
            }catch (Exception e){
                element = findElement(by, index);
            }
            threadSleep(SLEEPINMILLIS);
        }
        if(flag == false){
            Assert.fail("Element tiklanamadi!!! Locator : " + by + "  - timeout : " + WAIT_TIME);
            //assertMessage("Element tiklanamadi!!! Locator : " + by + "  - timeout : " + WAIT_TIME);
        }*/

        /*
        try {
            if (element == null){
                throw new RuntimeException("ELEMENT (" + by + (index.length > 0 ? index[0] : "")
                        + ") NOT EXIST; AUTOMATION DATAS MAY BE INVALID!");
            }else {
                waitUntilExpectedElement(by);
                for(int k = 0; k < 2; k++){
                    try {
                        for (int i = 0; i < 2; i++) {
                            scrollToVertical(element);
                            if(isElementDisplayed(by))
                                break;
                            threadSleep(300);
                        }
                        threadSleep(300);
                        element.click();
                        logger("--------------------------- " + message + "         ");
                        break;
                    }catch (Exception e){
                        if(k == 1) {
                            Assert.fail("Element tiklanamadi!! locator : " + by);
                        }
                    }
                    threadSleep(1500);
                    System.out.println("Element tiklanirken hata ile karsilasildi.. " + (k + 1) + ". deneme" + by);
                }
            }
        }catch (Exception e){
            Assert.fail("Element tiklanamadi!!! Locator : " + by);
        }*/
    }

    public void clickObjectByScrollCenter(By by, String message, int... index) {
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();
        boolean flag = false;
        System.out.println("Elementin bulunma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        while (isOperationInTimeout(finishTime, timeLeftSeconds(startTime, finishTime, WAIT_TIME))) {
            try {
                for (int i = 0; i < 2; i++) {
                    scrollToVertical(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                    scrollToCenter(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                    scrollToElement(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                }
                threadSleep(300);
                element.click();
                logger("--------------------------- " + message + "         ");
                flag = true;
                break;
            }catch (Exception e){
                element = findElement(by, index);
            }
            threadSleep(SLEEPINMILLIS);
        }
        if(flag == false){
            Assert.fail("Element tiklanamadi!!! Locator : " + by + "  - timeout : " + WAIT_TIME);
            //assertMessage("Element tiklanamadi!!! Locator : " + by + "  - timeout : " + WAIT_TIME);
        }



        /*
        try {
            if (element == null){
                throw new RuntimeException("ELEMENT (" + by + (index.length > 0 ? index[0] : "")
                        + ") NOT EXIST; AUTOMATION DATAS MAY BE INVALID!");
            }else {
                for(int k = 0; k < 2; k++){
                    try {
                        for (int i = 0; i < 2; i++) {
                            scrollToCenter(element);
                            if(isElementDisplayed(by))
                                break;
                            threadSleep(1000);
                        }
                        waitUntilExpectedElementClickable(element);
                        threadSleep(300);
                        element.click();
                        logger("--------------------------- " + message + "         ");
                        break;
                    }catch (Exception e){
                        if(k == 1) {
                            //Assert.fail("Element clickable degil!!! Locator : " + by);
                            //e.printStackTrace();
                            assertMessage("Element scroll edilerek tiklanamadi!!! Locator : " + by);
                        }
                    }
                    threadSleep(500);
                    System.out.println("Element tiklanirken hata ile karsilasildi.. " + (k + 1) + ". deneme" + by);
                }
            }
        }catch (Exception e){
            Assert.fail("Element scroll edilerek tiklanamadi!!! Locator : " + by);
            //assertMessage("Element scroll edilerek tiklanamadi!!! Locator : " + by);
        }*/
    }

    public void clickObjectByScrollCenterIfExist(By by, String message, int... index) {
        WebElement element = null;
        org.apache.log4j.BasicConfigurator.configure();
        long startTime = System.currentTimeMillis();
        element = findElement(by, index);
        long finishTime = System.currentTimeMillis();
        //System.out.println("Elementin bulunma suresi : " +(finishTime - startTime) + " milliseconds Locator : " + by);
        while (isOperationInTimeout(finishTime, timeLeftSeconds(startTime, finishTime, WAIT_TIME))) {
            try {
                for (int i = 0; i < 2; i++) {
                    scrollToCenter(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                    scrollToElement(element);
                    threadSleep(300);
                    if(isElementDisplayed(by))
                        break;
                }
                threadSleep(300);
                element.click();
                logger("--------------------------- " + message + "         ");
                break;
            }catch (Exception e){
                element = findElement(by, index);
            }
            threadSleep(SLEEPINMILLIS);
        }

        /*
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        element = findElementIfExist(by, index);

        try {
            //if (element == null){
            //    throw new RuntimeException("ELEMENT (" + by + (index.length > 0 ? index[0] : "")
            //            + ") NOT EXIST; AUTOMATION DATAS MAY BE INVALID!");
            //}else {
                for(int k = 0; k < 2; k++){
                    try {
                        for (int i = 0; i < 2; i++) {
                            scrollIntoView(by);
                            threadSleep(300);
                            if(isElementDisplayed(by))
                                break;
                            scrollToCenter(element);
                            if(isElementDisplayed(by))
                                break;
                            threadSleep(1000);
                        }
                        waitUntilExpectedElementClickableIfExist(element);
                        threadSleep(300);
                        element.click();
                        logger("--------------------------- " + message + "         ");
                        break;
                    }catch (Exception e){
                        if(k == 3) {
                            System.out.println("Element is not clickable : " + by);
                        }
                    }
                    threadSleep(500);
                    System.out.println("Element tiklanirken hata ile karsilasildi.. " + (k + 1) + ". deneme locator : " + by);
                }
            //}
        }catch (NullPointerException e){
            System.out.println("Element gelmedi.. : " + by);
        }*/
    }


    public void clickDoubleObject(By by, String message, int... index) {
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        //Actions action = new Actions(driver);
        try {
            element = findElement(by, index);
            if (element == null){
                throw new RuntimeException("ELEMENT (" + by + (index.length > 0 ? index[0] : "")
                        + ") NOT EXIST; AUTOMATION DATAS MAY BE INVALID!");
            }else {
                try {
                    for (int i = 0; i < 3; i++) {
                        //scrollToCenter(element);
                        //scrollToElement(element);
                        //scrollToElementHorizontal(element);
                        scrollToVertical(element);
                        if(isElementDisplayed(by))
                            break;
                        threadSleep(300);
                    }
                    waitUntilExpectedElementClickable(element, 10);
                    threadSleep(300);
                    //action.doubleClick();
                    element.click();
                    logger("---------------------------" + message + "         ");
                }catch (Exception e){
                    Assert.fail("Element clickable degil!!! Locator : " + by);
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            Assert.fail("Element cift tiklanamadi!!! Locator : " + by);
            //assertMessage("Element cift tiklanamadi!!! Locator : " + by);
        }
    }

    public void clickObjectScrollToCenter(By by, String message, int... index) {
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        element = findElement(by, index);
        try {
            if (element == null){
                throw new RuntimeException("ELEMENT (" + by + (index.length > 0 ? index[0] : "")
                        + ") NOT EXIST; AUTOMATION DATAS MAY BE INVALID!");
            }else {
                try {
                    for (int i = 0; i < 2; i++) {
                        scrollToCenter(element);
                        //scrollToElement(element);
                        //scrollToElementHorizontal(element);
                        //scrollToVertical(element);
                        if(isElementDisplayed(by))
                            break;
                        threadSleep(500);
                        scrollIntoView(by);
                        threadSleep(300);
                        if(isElementDisplayed(by))
                            break;
                    }
                    waitUntilExpectedElementClickable(element);
                    threadSleep(300);
                    element.click();
                    logger("---------------------------" + message + "         ");
                }catch (Exception e){
                    Assert.fail("Element clickable degil Locator : " + by);
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            Assert.fail("Element merkeze scroll edilerek tiklanamadi!!! Locator : " + by);
            e.getMessage();
            //assertMessage("Element merkeze scroll edilerek tiklanamadi!!! Locator : " + by);
        }
    }

    private void waitForJQuery() {
        (new WebDriverWait(driver, WAIT_TIME)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                JavascriptExecutor js = (JavascriptExecutor) d;
                return (Boolean) js.executeScript("return !!window.jQuery && window.jQuery.active == 0");
            }
        });
    }

    private List<WebElement> getObjectByPresentOfElementList(By by) {
        waitForJQuery();
        return (new WebDriverWait(driver, WAIT_TIME))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public List<WebElement> getElementsBy(By by) {
        return getObjectByPresentOfElementList(by);
    }

    public static String convertTurkishChar(String string) {
        string = string.replace("ç", "c");
        string = string.replace("ö", "o");
        string = string.replace("ş", "s");
        string = string.replace("ğ", "g");
        string = string.replace("ü", "u");
        string = string.replace("ı", "i");
        string = string.replace("Ç", "C");
        string = string.replace("Ö", "O");
        string = string.replace("Ş", "S");
        string = string.replace("Ğ", "G");
        string = string.replace("Ü", "U");
        string = string.replace("İ", "I");
        return string;
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl().trim();
    }

    public void goBack() {
        driver.navigate().back();
    }

    public void waitForElement(WebDriver driver, int seconds, By elementBy) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, seconds, SLEEPINMILLIS);
            wait.until(ExpectedConditions.presenceOfElementLocated(elementBy));
        } catch (TimeoutException e) {
            System.out.println("Beklenen element = " + elementBy + ", " + seconds + " saniye içerisinde bulunamadi.");
        }
    }

    public void waitForElement(int seconds, By elementBy) {

        try {
            WebDriverWait wait = new WebDriverWait(this.driver, seconds, SLEEPINMILLIS);
            wait.until(ExpectedConditions.presenceOfElementLocated(elementBy));
        } catch (TimeoutException e) {
            System.out.println("Beklenen element = " + elementBy + ", " + seconds + " saniye içerisinde bulunamadi.");
        }
    }


    public String convertToLink(String name) {
        String link = name.replace(" ", "-");
        link += "/";
        convertTurkishChar(link);
        return link;
    }

    public void scrollIntoView(By by){
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        WebElement element = findElement(by);
        jse.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToElement(By by, int... index) {
        WebElement element = findElement(by, index);
        for(int i = 0; i < 4; i++){
            scrollToElement(element);
            threadSleep(500);
            if(element.isDisplayed())
                break;
        }

    }

    public void scrollToElement(WebElement element) {
        if (element != null) {
            scrollTo(element.getLocation().getX(), element.getLocation().getY());
        }
    }

    public void scrollToElementHorizontal(WebElement element) {
        if (element != null) {
            scrollTo(1000, element.getLocation().getY());
        }
    }

    public void scrollToElementWithAction(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).moveToElement(element).build().perform();
    }

    public void scrollTo(int x, int y) {
        String jsStmt = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(jsStmt, true);
    }

    public void scrollToElementWithJS(WebElement element) {
        scrollToTop();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
    }

    public Object executeJS(String jsStmt, boolean wait) {
        return wait ? getJSExecutor().executeScript(jsStmt, "") : getJSExecutor().executeAsyncScript(jsStmt, "");
    }

    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }


    private void hoverElement(By by, String message, boolean click, int... index) {
        Actions action = new Actions(driver);
        action.moveToElement(findElement(by, index)).build().perform();
        if (click) {
            clickObject(by, message, index);
        }
    }

    public void hoverElement(By by, String message, int... index) {
        hoverElement(by, message, false, index);
    }

    public boolean isElementDisplayed(By by, String timeout, int... index) {
        boolean found = false;
        try {
            if (findElementIfExist(by, timeout, index) != null)
                found = true;
        } catch (NullPointerException e) {
            found = false;
        }
        return found;
    }

    public boolean isElementDisplayed(By by, int... index) {
        boolean displayed = false;
        WebElement element = findElement(by, index);
        try {
            if (element != null && element.isDisplayed()) {
                displayed = true;
            }
        } catch (NullPointerException e) {
            displayed = false;
        }
        return displayed;
    }

    public boolean isElementExists(By by, int... index) {
        String jsStmt = index.length == 0 || index[0] < 0 ? String.format("return $('%s').size()>0", by)
                : String.format("return $('%s').size()>0 && $('%s').eq(%d).size()>0", by, by,
                index[0]);
        Object result = executeJS(jsStmt, true);

        return result != null && "true".equalsIgnoreCase(result.toString());
    }

    public boolean isElementExist(By by, String timeout) {
        int seconds = Integer.parseInt(timeout);
        for(;;) {
            if(seconds < 0)
                break;
            try {
                scrollToCenter(driver.findElement(by));
                return true;
            }catch (Exception e) { }
            threadSleep(2000);
            seconds -= 1;
        }
        return false;
    }

    public boolean isElementInView(By by, int... index) {
        String jsStmt = index.length == 0 || index[0] < 0
                ? String.format("return $('%s').size()>0;", by, by)
                : String.format("return $('%s').size()>0;", by, by, index[0]);
        Object result = executeJS(jsStmt, true);

        return result != null && "true".equalsIgnoreCase(result.toString());
    }

    public boolean compareContents(List<String> contentFromPage, List<String> contentFromDB) {
        Assert.assertTrue("DB'den gelen kategori sayfasiyla, sayfadaki kategori sayisi farkli!",
                contentFromPage.size() == contentFromDB.size());
        boolean equal = contentFromPage.size() == contentFromDB.size();
        for (int i = 0; i < contentFromPage.size(); i++) {
            System.out.println(contentFromPage.get(i) + " " + contentFromDB.get(i));
            if (!contentFromPage.get(i).equalsIgnoreCase(contentFromDB.get(i))) {
                equal = false;
            }
        }
        return equal;
    }


    public static String Md5(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());

        byte byteData[] = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void scrollSmoothDown(int durationOfScroll){
        long timeWhenStarting = System.currentTimeMillis() / 1000L;
        while (System.currentTimeMillis() / 1000L - timeWhenStarting < durationOfScroll) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,50)", "");
        }
    }

    public void scrollSmoothUp(int durationOfScroll){
        long timeWhenStarting = System.currentTimeMillis() / 1000L;
        while (System.currentTimeMillis() / 1000L - timeWhenStarting < durationOfScroll) {
            ((JavascriptExecutor) driver).executeScript("window.scrollBy(10,0)", "");
        }
    }

    /**
     * Gönderilen değere göre veriyi istenilen yerde(liste gibi) arar
     */
    public String elementFound(By by, int forFirstIndex, int forICount, String expectedValue){
        ArrayList<String> actualList;
        actualList = (ArrayList<String>) getWebElementsText(by);
        String returnValue = null;

        for (int i = forFirstIndex ; i < actualList.size() ; i = i + forICount){
            if (actualList.get(i).equals(expectedValue)){
                returnValue = expectedValue;
                break;
            }
        }

        System.out.println("actualList: " + actualList);
        System.out.println("returnValue: " + returnValue);

        return returnValue;
    }

    public void selectElementWithVisibleText(By by, String text) {
        long startTime = System.currentTimeMillis();
        WebElement element = findElement(by, "5");
        Select dropdown = new Select(element);
        //long endTime = System.currentTimeMillis();
        boolean flag = false;
        System.out.println("Selectbox size : " + dropdown.getOptions().size());
        while (isOperationInTimeout(startTime, WAIT_TIME)){
            try{
                if((element.getAttribute("value").equals(text) || element.getAttribute("title").equals(text) || element.getText().equals(text))){
                    flag = true;
                    break;
                }
                dropdown.selectByVisibleText(text);
                if(!(element.getAttribute("value").equals(text) || element.getAttribute("title").equals(text) || element.getText().equals(text)))
                    dropdown.selectByVisibleText(text);
                if((element.getAttribute("value").equals(text) || element.getAttribute("title").equals(text) || element.getText().equals(text))){
                    flag = true;
                    break;
                }
                flag = true;
                break;
            }catch (Exception e) {
                element = findElement(by, "2");
                try {
                    scrollToElementWithAction(element);
                    dropdown.selectByVisibleText(text);
                    if(!(element.getAttribute("value").equals(text) || element.getAttribute("title").equals(text) || element.getText().equals(text))){
                        clickObject(by, "selectbox tiklandi..");
                        dropdown.selectByVisibleText(text);
                        keyboardEnter();
                        threadSleep(300);
                    }
                    flag = true;
                    break;

                } catch (Exception ex) {
                    element = findElement(by, "2");
                    try {
                        scrollToElement(element);
                        dropdown.selectByVisibleText(text);
                        if(!(element.getAttribute("value").equals(text) || element.getAttribute("title").equals(text) || element.getText().equals(text))){
                            clickObject(by, "selectbox tiklandi..");
                            dropdown.selectByVisibleText(text);
                            keyboardEnter();
                            threadSleep(300);
                        }
                        flag = true;
                        break;
                    } catch (Exception exc) {
                        element = findElement(by, "2");
                        try {
                            scrollToCenter(element);
                            dropdown.selectByVisibleText(text);
                            if(!(element.getAttribute("value").equals(text) || element.getAttribute("title").equals(text) || element.getText().equals(text))){
                                clickObject(by, "selectbox tiklandi..");
                                dropdown.selectByVisibleText(text);
                                keyboardEnter();
                                threadSleep(300);
                            }
                            flag = true;
                            break;
                        } catch (Exception exce) { }
                    }
                }
            }
        }
        if(flag == false){
            Assert.fail("Element secilemedi!!! Locator : " + by + " - timeout : " + WAIT_TIME);
            //assertMessage("Element secilemedi!!! Locator : " + by + " - timeout : " + WAIT_TIME);
        }
    }

    public void selectElementWithIndex(By by, int index) {
        long startTime = System.currentTimeMillis();
        WebElement element = findElement(by);
        //long endTime = System.currentTimeMillis();
        boolean flag = false;
        Select dropdown;
        while (isOperationInTimeout(startTime, WAIT_TIME)) {
            try {
                dropdown = new Select(element);
                dropdown.selectByIndex(index);
                flag = true;
                break;
            } catch (Exception e) {
                element = findElement(by, "2");
                dropdown = new Select(element);
                try {
                    scrollToElementWithAction(element);
                    dropdown.selectByIndex(index);
                    flag = true;
                    break;
                } catch (Exception ex) {
                    element = findElement(by, "2");
                    dropdown = new Select(element);
                    try {
                        scrollToElement(element);
                        dropdown.selectByIndex(index);
                        flag = true;
                        break;
                    } catch (Exception exc) {
                        element = findElement(by, "2");
                        dropdown = new Select(element);
                        try {
                            scrollToCenter(element);
                            dropdown.selectByIndex(index);
                            flag = true;
                            break;
                        } catch (Exception exce) {
                        }
                    }
                }
            }
        }
        if (flag == false) {
            Assert.fail("Element secilemedi!!! Locator : " + by + " - timeout : " + WAIT_TIME);
            //assertMessage("Element secilemedi!!! Locator : " + by + " - timeout : " + WAIT_TIME);
            // }
        }
    }

    public void selectElementWithIndex (By by,int index, int timeout){
        long time1 = System.currentTimeMillis();
        waitForPageLoaded();
        long time2 = System.currentTimeMillis();
        threadSleep(1000);
        long startTime = System.currentTimeMillis();
        WebElement element = findElementForSelect(by);
        long endTime = System.currentTimeMillis();
        boolean flag = false;
        while (isOperationInTimeout(endTime, timeLeftSeconds(startTime, endTime, timeout))) {
            try {
                Select dropdown = new Select(element);
                for (int i = 0; i < 2; i++) {
                    scrollToCenter(element);
                    if (element.isDisplayed())
                        break;
                    threadSleep(500);
                }
                dropdown.selectByIndex(index);
                flag = true;
                break;
            } catch (Exception e) {
                element = findElementForSelect(by);
            }
            threadSleep(SLEEPINMILLIS);
        }
        if (flag == false) {
            Assert.fail("Element secilemedi!!! Locator : " + by + " - timeout : " + WAIT_TIME);
            //assertMessage("Element secilemedi!!! Locator : " + by + " - timeout : " + WAIT_TIME);
        }
    }
    public String createRandomAlphanumeric ( int digit){
        return RandomStringUtils.randomAlphanumeric(digit);
    }
    public String createRandomNumeric ( int digit){
        return RandomStringUtils.randomNumeric(digit);
    }
    public String getNowTime (String dateTypeFormat){
        Date nowTime = new Date();
        DateFormat dateFormat = null;
         if (dateTypeFormat.equals("yyyy/MM/dd")) {
             dateFormat = new SimpleDateFormat("yyyy/MM/dd");
         } else if (dateTypeFormat.equals("dd/MM/yyyy")) {
             dateFormat = new SimpleDateFormat("dd/MM/yyyy");
         }
         assert dateFormat != null;
         return String.valueOf(dateFormat.format(nowTime));
    }

    public void clickObjectFk (By by, String message,int...index){
        WebElement element;
        org.apache.log4j.BasicConfigurator.configure();
        try {
            element = findElement(by);
            if (element == null) {
                throw new RuntimeException("ELEMENT (" + by + "," + (index.length > 0 ? index[0] : "")
                        + ") NOT EXIST; AUTOMATİON DATAS MAY BE INVALID!");
            }
            element.click();
            logger("---------------------------" + message + "         ");
        } catch (NullPointerException e) {
            Assert.fail("Element Not Exist: " + by);
            //assertMessage("Element bulunamadi!!! Locator : " + by);
        }
    }
    public void scrollToTop () {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, 0)");
    }

    public void scrollToCenter (WebElement element){
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'center'})",
                element);
    }

    public void scrollToVertical (WebElement element){
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'nearest'})",
                element);
    }

    public void scrollToIfNeeded (WebElement element){
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoViewIfNeeded()",
                element);
    }

    public void selectAll () {
        //clickObject(By.xpath("//embed"),"Calısıyor...");
        //driver.findElement(By.xpath("//embed")).click();
        //clickWithJS(By.xpath("//embed"));
        //threadSleep(500);
        driver.findElement(By.xpath("//embed")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
    }

    public void copySelectedText () {
        driver.findElement(By.xpath("//embed")).sendKeys(Keys.chord(Keys.CONTROL, "c"));
        //driver.findElement(By.xpath("//body")).sendKeys(Keys.CONTROL, "c");
    }

    public String readClipboard () throws UnsupportedFlavorException, IOException {
        return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
    }

    public String readClipboardTryCatch () {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (Exception e) {
        }
        return "false";
    }

    public boolean isWordExistInCurrentPdf (String text,int timeOut){
        System.out.println("----------************ Aranan Kelime : " + text);
        WebDriverWait w = new WebDriverWait(driver, timeOut, SLEEPINMILLIS);
        w.until(ExpectedConditions.elementToBeClickable(By.xpath("//embed"))).click();
        threadSleep(1000);
        selectAll();
        threadSleep(1000);
        copySelectedText();
        try {
            System.out.println(readClipboard());
            return readClipboard().contains(text);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isWordExistInCurrentPdfMBbee (String text,int timeOut){
        System.out.println("----------************ Aranan Kelime : " + text);
        waitUntilExpectedElement(By.xpath("//embed"));
        for (int i = 0; i < timeOut / 2; i++) {
            clickObject(By.xpath("//embed"), "Pdf tiklandi..");
            //Actions action = new Actions(driver);
            //action.moveToElement(driver.findElement(By.xpath("//embed"))).click().perform();
            threadSleep(1000);
            selectAll();
            threadSleep(1000);
            copySelectedText();
            if (readClipboardTryCatch().contains(text)) {
                System.out.println("Aranan kelime PDF icerisinde bulundu..");
                return true;
            }
            threadSleep(1000);
        }
        return false;
    }

    public void clickWithJS (By by,int...index){
        WebElement element = findElement(by, index);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public void clickWithJS (WebElement element){
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public void clickWithJSIfExist (WebElement element){
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        try{
            executor.executeScript("arguments[0].click();", element);
        }catch (Exception e){}
    }

    public void clickWithJS (By by, String message,int...index){
        WebElement element = findElement(by, index);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
        logger(message);
    }

    public void clickWithJSIfExist (By by, String message,int...index){
        for (int i = 0; i < 4; i++) {
            try {
                WebElement element = findElementIfExist(by, index);
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", element);
                logger(message);
            } catch (Exception e) {
            }
            threadSleep(500);
        }
    }

    public void clickWithJSIfExist (By by, String message, String timeout,int...index){
        findElementIfExist(by, timeout, index);
        for (int i = 0; i < 2; i++) {
            try {
                clickWithJS(by);
                logger(message);
                break;
            } catch (Exception e) { }
            threadSleep(500);
        }
    }

    public void clickWithJS (By by, String message){
        clickWithJS(by);
        if (!message.equals(""))
            logger("---------------------------" + message + "         ");
    }

    public String getRandomAlphabetic ( int length){
        byte[] randArray = new byte[length];
        new Random().nextBytes(randArray);
        return new String(randArray, Charset.forName("UTF-8"));
    }

    public String getRandomNumeric ( int min, int max){
        Random random = new Random();
        long number = random.nextInt((max - min) + 1) + min;
        return String.valueOf(number);
    }

    public boolean isOperationInTimeout ( long startTime, int timeout){
        return ((timeout * 1000) + startTime) - System.currentTimeMillis() > 0;
    }

    public int timeLeftSeconds ( long startTime, long endTime, long timeout){
        //System.out.println("Kalan zaman (Saniye) : " + (timeout - ((endTime - startTime)/1000)));
        return (int) (timeout - ((endTime - startTime) / 1000));
    }

    public void waitForPageLoaded () {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Sayfa yuklenemedi!!!!");
            //assertMessage("Sayfa yuklenemedi!!!!");
        }
    }

    public void waitForPageLoaded ( int timeout){
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
            }
        };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, timeout);
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Sayfa yuklenemedi!!!!");
            //assertMessage("Sayfa yuklenemedi!!!!");
        }
    }

    public void assertMessage (String assertionMessage){
        assert false : assertionMessage;
    }
    public void scrollUntilDisplayed (By by,int...index){
        WebElement element = findElement(by, index);
        for (int i = 0; i < 3; i++) {
            try {
                if (isElementDisplayed(by))
                    break;
                scrollToCenter(element);
                threadSleep(500);
                if (isElementDisplayed(by))
                    break;
            } catch (Exception e) {
                element = findElement(by, "3", index);
            }
            try {
                if (isElementDisplayed(by))
                    break;
                scrollToElementWithAction(element);
                threadSleep(300);
                if (isElementDisplayed(by))
                    break;
            } catch (Exception e) {
                element = findElement(by, "3", index);
            }
            try {
                scrollToElement(element);
                threadSleep(300);
                if (isElementDisplayed(by))
                    break;
            } catch (Exception e) {
                element = findElement(by, "3", index);
            }
            try {
                scrollToTop();
                scrollSmoothDown(5);
                threadSleep(300);
                if (isElementDisplayed(by))
                    break;
            } catch (Exception e) {
                element = findElement(by, "3", index);
            }
        }
    }

    public void scrollUntilDisplayed (By by, String timeout,int...index){
        long startTime = System.currentTimeMillis();
        int iTimeout = Integer.valueOf(timeout);
        WebElement element = findElement(by, "5", index);
        while (isOperationInTimeout(startTime, iTimeout)) {
            try {
                if (isElementDisplayed(by))
                    break;
                scrollToCenter(element);
                threadSleep(500);
                if (isElementDisplayed(by))
                    break;
            } catch (Exception e) {
                element = findElement(by, "3", index);
            }
            try {
                if (isElementDisplayed(by))
                    break;
                scrollToElementWithAction(element);
                threadSleep(300);
                if (isElementDisplayed(by))
                    break;
            } catch (Exception e) {
                element = findElement(by, "3", index);
            }
            try {
                scrollToElement(element);
                threadSleep(300);
                if (isElementDisplayed(by))
                    break;
            } catch (Exception e) {
                element = findElement(by, "3", index);
            }
            try {
                scrollToTop();
                scrollSmoothDown(5);
                threadSleep(300);
                if (isElementDisplayed(by))
                    break;
            } catch (Exception e) {
                element = findElement(by, "3", index);
            }
        }
    }

    public void scrollToElementsPositionWithJS (By by,int timeout, int...index){
        long startTime = System.currentTimeMillis();
        int iTimeout = Integer.valueOf(timeout);
        WebElement element = findElement(by, "5", index);
        int x, y;
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        while (isOperationInTimeout(startTime, iTimeout)) {
            try {
                x = element.getLocation().getX();
                y = element.getLocation().getY();
                //System.out.println("Element Locator : " + by + " x : " + x + " y : " + y);
                js.executeScript("window.scrollTo(" + x + ", " + y + ")");
            } catch (Exception e) {
                element = findElement(by, "2", index);
                if (isElementDisplayed(by)) {
                    System.out.println("Element goruldu.");
                    break;
                }
            }
        }
    }
}