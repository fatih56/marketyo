Hepsiburada Case
=====================
Created by FTH on 12.10.2020

This is an executable specification file which follows markdown syntax.
Every heading in this file denotes a scenario. Every bulleted point denotes a step.
     


Login
----------------------------------------------------------------------------------
* Hepsiburada WebSite Ac url:"https://www.hepsiburada.com/"
* Giris Yap Menu Ac
* Giris Yap Menu Secim yap. Menu Secim:"Giriş Yap"
* Giris Yap email:"config",password:"config"
* "3" saniye Bekle
* Giris Yap Menu Ac
* Giris Yap Menu Secim yap. Menu Secim:"Çıkış Yap"

Login Error
----------------------------------------------------------------------------------
* Hepsiburada WebSite Ac url:"https://www.hepsiburada.com/"
* Giris Yap Menu Ac
* Giris Yap Menu Secim yap. Menu Secim:"Giriş Yap"
* Giris Yap email:"aaa@fff.com",password:"1234"
* Login mesaj kontrol et.loginMessage:"Bilgileriniz eksik veya hatalı."

Login Error Log
----------------------------------------------------------------------------------
* Hepsiburada WebSite Ac url:"https://www.hepsiburada.com/"
* Giris Yap Menu Ac
* Giris Yap Menu Secim yap. Menu Secim:"Giriş Yap"
* Giris Yap email:"aaa@fff.com",password:"1234"


Search
----------------------------------------------------------------------------------
* Hepsiburada WebSite Ac url:"https://www.hepsiburada.com/"
* Arama yap.Aranacak Txt:"klima arçelik"
* Arama sonucu txt dosya yaz.aramaAnahtari:"klima arçelik",dosyaName:"searchResult.txt",dosyaTemizlensinMi:"true"

Search ConfigFile
----------------------------------------------------------------------------------
* Hepsiburada WebSite Ac url:"https://www.hepsiburada.com/"
* Arama yap.Aranacak Txt ConfigFile
* Arama sonucu txt dosya yaz.aramaAnahtari:"config",dosyaName:"searchResult.txt",dosyaTemizlensinMi:"true"

Search empty result
----------------------------------------------------------------------------------
* Hepsiburada WebSite Ac url:"https://www.hepsiburada.com/"
* Arama yap.Aranacak Txt:"xaxaxaxaxa"
* Arama sonuc bos gelme Kontrol:arananKelime"xaxaxaxaxa"