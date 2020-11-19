package com.hepsiburada;

import com.hepsiburada.utilities.base.BasePage;
import com.hepsiburada.utilities.base.BaseTest;
import com.hepsiburada.utilities.helper.Configuration;
import com.hepsiburada.utilities.helper.txtFile;
import com.thoughtworks.gauge.Step;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class loginPage {
    private WebDriver driver = BaseTest.getDriver();
    private BasePage basePage = new BasePage(driver);
    private Configuration conf = new Configuration();

    public By loginEmail = By.xpath("//input[contains(@id,'txtUserName')]");
    public By loginPassword = By.xpath("//input[contains(@id,'txtPassword')]");
    public By girisYapButton = By.id("btnLogin");
    public By errorMessage = By.xpath("(((//div[contains(@type,'ERROR')])[2]//div)[1]//div)[2]");
    //bu xpath boyle yazılma sebebi unique herhangi bir değer yok ve class nameleri bile şifrelenmiş

    @Step("Login Email Gir. Email:<email>")
    public void loginEmailFill(String email) {
        if (email.equalsIgnoreCase("config")) {
            basePage.waitUntilExpectedElement(loginEmail);
            basePage.fillInputField(loginEmail, conf.getUserName());

        } else {


            basePage.waitUntilExpectedElement(loginEmail);
            basePage.fillInputField(loginEmail, email);
        }


    }

    @Step("Login Password Gir. Password:<password>")
    public void loginPasswordFill(String password) {
        if (password.equalsIgnoreCase("config")) {
            basePage.waitUntilExpectedElement(loginPassword);
            basePage.fillInputField(loginPassword, conf.getPassword());
        } else {
            basePage.waitUntilExpectedElement(loginPassword);
            basePage.fillInputField(loginPassword, password);
        }
    }


    @Step("Login Giris Yap buton Tikla")
    public void giriyapButtonClick() {
        basePage.waitUntilExpectedElementClickable(girisYapButton);
        basePage.findElement(girisYapButton).click();

    }

    @Step("Login mesaj kontrol et.loginMessage:<loginMessage>")
    public void loginMessageKontrol(String loginMessage) {
        String messageTxt = "";

        basePage.waitUntilExpectedElement(errorMessage);
        messageTxt = basePage.findElement(errorMessage).getText();

        Assert.assertEquals(loginMessage, messageTxt);

    }

    @Step("Login Error Message log txt file")
    public void logErrorLogin() throws IOException {
        String errorMessageTxt = "";

        if(driver.findElements(errorMessage).size()!=0) {
            errorMessageTxt = basePage.findElement(errorMessage).getText();
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());

            txtFile.writeTxt("src/test/resources/loginError.txt", timeStamp + " " + errorMessageTxt + "\n");
        }
    }


}
