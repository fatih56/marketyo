package com.hepsiburada;

import com.hepsiburada.utilities.base.BasePage;
import com.hepsiburada.utilities.base.BaseTest;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Objects;
import java.util.function.Function;

public class general {


    private WebDriver driver = BaseTest.getDriver();
    private BasePage basePage = new BasePage(driver);


    public By anasayfaLink = By.xpath("//a[contains(@class,'logo-hepsiburada')]");

   @Step("Buton <butonText> tikla")
    public void butonTikla(String butonTxt) {
        By butonBy = By.xpath("//button[contains(text(),'" + butonTxt + "')]");

        basePage.waitUntilExpectedElementClickable(butonBy);
        basePage.findElement(butonBy).click();

    }

    @Step("Buton mevcutsa <butonText> tikla")
    public void buttonClickIfExist(String butonTxt) {
        By butonBy = By.xpath("//button[contains(text(),'" + butonTxt + "')]");

        if (driver.findElements(butonBy).size() != 0) {
            basePage.waitUntilExpectedElementClickable(butonBy);
            basePage.findElement(butonBy).click();
        }
    }


    @Step("<saniye> saniye Bekle")
    public void bekle(int saniye)
    {
        basePage.threadSleep(saniye*1000);
    }

    @Step("Anasayfaya Git")
    public void anasayfaGit() {
        basePage.waitUntilExpectedElement(anasayfaLink);
        basePage.findElement(anasayfaLink).click();

    }


@Step("Link <linkText> tikla")
    public void linkTextTikla(String linkTxt)
{ By linkBy=By.xpath("//a[contains(text(),'"+linkTxt+"')]");
    basePage.waitUntilExpectedElement(linkBy);
    basePage.findElement(linkBy).click();


}


}

