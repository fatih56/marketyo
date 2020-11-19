package com.hepsiburada;

import com.hepsiburada.utilities.base.BasePage;
import com.hepsiburada.utilities.base.BaseTest;
import com.hepsiburada.utilities.helper.Configuration;
import com.hepsiburada.utilities.helper.txtFile;
import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.*;

public class searchPage {
    private Configuration conf = new Configuration();
    private WebDriver driver = BaseTest.getDriver();
    private BasePage basePage = new BasePage(driver);

    public By ilkUrunSec = By.xpath("(//ul[contains(@class,'product-list')]//li//a)[1]");
    public By urunTitle = By.xpath("//h3[contains(@class,'product-title title')]");
    public By urunPage = By.xpath("//a[contains(@class,'page')]");
    public By aramaBulunamadiMessage = By.xpath("//li[contains(@class,'search-item')]//h3");

    @Step("Ilk Urun Sec")
    public void urunSec() {

        basePage.waitUntilExpectedElement(ilkUrunSec);
        basePage.findElement(ilkUrunSec).click();


    }

    @Step("Arama sonucu txt dosya yaz.aramaAnahtari:<aramaAnahtari>,dosyaName:<dosyaName>,dosyaTemizlensinMi:<dosyaTemizlensinMi>")
    public void aramaSonucuYaz(String aramaAnahtari, String dosyaName, boolean dosyaTemizlensinMi) throws IOException {
        List products = new ArrayList();
        List<WebElement> elementName;
        if (aramaAnahtari.equalsIgnoreCase("config")) {
            products.add(conf.getSearhTerm());
        } else {
            products.add(aramaAnahtari);

        }
        basePage.waitForPageLoaded();
        System.out.println(driver.findElements(urunTitle).size());
        Assert.assertNotEquals(0,driver.findElements(urunTitle).size());
        int sizePage = driver.findElements(urunPage).size();
        System.out.println("SizePage:" + sizePage);
        By lastpage = By.xpath("(//a[contains(@class,'page')])[" + sizePage + "]");
        String lastPageNumber = basePage.findElement(lastpage).getText();//hepsi burada da searchte pagelerin numarası hepsi gözükmüyor bu yüzden son numarayı alıp ona göre işlem yapıcam.
        for (int sayfaNumara = 1; sayfaNumara <= Integer.parseInt(lastPageNumber); sayfaNumara++) {
            basePage.waitUntilExpectedElement(urunTitle);
            By page = By.xpath("//a[contains(@class,'page') and contains(text(),'" + sayfaNumara + "')]");
            basePage.findElement(page).click();
            basePage.waitForPageLoaded();
            products.add(String.valueOf(sayfaNumara));
            elementName = driver.findElements(urunTitle);
            for (int i = 0; i < elementName.size(); i++) {
                //    System.out.println(elementName.get(i).getAttribute("title"));
                elementName.get(i).getAttribute("title");
                products.add(elementName.get(i).getAttribute("title"));

            }
        }

        txtFile.multiLine("src/test/resources/" + dosyaName, products, !dosyaTemizlensinMi);
    }

    @Step("Arama sonuc bos gelme Kontrol:arananKelime<arananKelime>")
    public void aramaSonucBosKontrol(String arananKelime) {
        String mesajTxt = "";
        basePage.waitUntilExpectedElement(aramaBulunamadiMessage);

        mesajTxt = basePage.findElement(aramaBulunamadiMessage).getText();
        System.out.println("mesajTxt:" + mesajTxt);

        Assert.assertEquals("“" + arananKelime + "” ile ilgili sonuç bulunamamıştır.", mesajTxt);

    }

}
