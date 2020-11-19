package com.hepsiburada.utilities.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class Configuration {

    private static Configuration instance;
    private Properties configProps = new Properties();

private String userName;
private String password;
private String browser;
private String searhTerm;



    public static Configuration getInstance() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    private static synchronized void createInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
    }

    public Configuration() {
        InputStream is = null;

        try {
           is = ClassLoader.getSystemResourceAsStream("config.txt");
            Reader reader = new InputStreamReader(is, "UTF-8");
            configProps.load(reader);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
              this.userName=configProps.getProperty("userName");
              this.password=configProps.getProperty("password");
              this.browser=configProps.getProperty("browser");
this.searhTerm=configProps.getProperty("searhTerm");

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String[][] stringTo2DArray(String string) {
        String[][] datas = new String[(string.split("\\|\\|")).length][(string.split("\\|\\|")[0]).split(",").length];

        for (int i = 0; i < (string.split("\\|\\|")).length; i++) {
            datas[i] = (string.split("\\|\\|")[i]).split(",");
        }
        return datas;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getBrowser() {
        return browser;
    }

    public String getSearhTerm() {
        return searhTerm;
    }
}

