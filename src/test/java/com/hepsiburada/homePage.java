package com.hepsiburada;

import com.hepsiburada.utilities.base.BasePage;
import com.hepsiburada.utilities.base.BaseTest;
import com.hepsiburada.utilities.helper.Configuration;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class homePage {

    private WebDriver driver = BaseTest.getDriver();
    private BasePage basePage = new BasePage(driver);
    private Configuration conf=new Configuration();
    public By girisYapMenu=By.xpath("//div[contains(@id,'myAccount')]");
public By aramaCubugu=By.xpath("(//div[contains(@id,'SearchBoxOld')]//input)[1]");
public By urunAraButton=By.xpath("//div[contains(@class,'SearchBoxOld-buttonContainer') and contains(text(),'ARA')]");

    @Step("Hepsiburada WebSite Ac url:<url>")
    public void navigatewebSite(String url)
    {


    driver.navigate().to(url);

    }

   @Step("Giris Yap Menu Ac")
    public void girisYapMenuClick()
   {
       basePage.waitUntilExpectedElement(girisYapMenu);

       basePage.findElement(girisYapMenu).click();


   }

   @Step("Giris Yap Menu Secim yap. Menu Secim:<menuSecim>")
public void girisYapMenuSec(String menuSecim)
   {By menuSecimBy=By.xpath("//div[contains(@id,'myAccount')]//a[contains(text(),'"+menuSecim+"')]");

   basePage.waitUntilExpectedElement(menuSecimBy);
   basePage.findElement(menuSecimBy).click();

   }

   @Step("Arama yap.Aranacak Txt:<aranacakTxt>")
    public void aramaYap(String aracakTxt)
   {
       basePage.waitUntilExpectedElement(aramaCubugu);

       basePage.fillInputField(aramaCubugu,aracakTxt);
       basePage.findElement(urunAraButton).click();

   }

    @Step("Arama yap.Aranacak Txt ConfigFile")
    public void aramaYap()
    {
        basePage.waitUntilExpectedElement(aramaCubugu);

        basePage.fillInputField(aramaCubugu,conf.getSearhTerm());
        basePage.findElement(urunAraButton).click();

    }


   @Step("Ana Menu Sec. MenuTxt:<menuTxt>")
    public void anaMenuSec(String anaMenuTxt)
    {By anaMenuBy=By.xpath("//div[contains(@id,'NavigationDesktop')]//span[contains(text(),'"+anaMenuTxt+"')]");

    basePage.waitUntilExpectedElement(anaMenuBy);

    basePage.hoverElement(anaMenuBy,"Ana menu");


    }

    @Step("Alt liste Sec. ana menu txt=<anaMenuTxt>,alt liste txt:<altListeTxt>")
    public void altMenuSec(String anaMenuTxt,String altMenuTxt)
    {
        By altMenuBy=By.xpath("//div[contains(@id,'NavigationDesktop')]//span[contains(text(),'"+anaMenuTxt+"')]//ancestor::li//a//span[contains(text(),'"+altMenuTxt+"')]//parent::a");

        basePage.waitUntilExpectedElement(altMenuBy);

        basePage.findElement(altMenuBy).click();

    }
//arama sayfası ürünler
//    (//ul[contains(@class,'product-list')]//li//a)[1]
// menu seçim
//span[contains(text(),'Kitap, Müzik, Film, Hobi')]//ancestor::li//a//span[contains(text(),'Öykü')]//parent::a

}
