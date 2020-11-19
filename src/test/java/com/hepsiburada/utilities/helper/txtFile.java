package com.hepsiburada.utilities.helper;

import org.junit.Assert;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class txtFile {


    public static void writeTxt(String fileName, String value) throws IOException {
        BufferedWriter bufferedWriter = null;

        File file = new File(fileName);

        if (!file.exists()) {

            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file, true);
        bufferedWriter = new BufferedWriter(fileWriter);


        bufferedWriter.write(value);
        bufferedWriter.flush();
        bufferedWriter.close();
        System.out.println("file write Success");

    }

    public static void multiLine(String fileName, List<String> product, boolean dosyaTemizlensinMi) throws IOException {
        BufferedWriter bufferedWriter = null;

        File file = new File(fileName);

        if (file.exists()) {

            file.createNewFile();
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());

        FileWriter fileWriter = new FileWriter(file, dosyaTemizlensinMi);
        bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("************** "+timeStamp +" ***************\n");
            bufferedWriter.flush();

        for (int i = 0; i < product.size(); i++) {
            bufferedWriter.write(product.get(i) + "\n");

            bufferedWriter.flush();
        }

        bufferedWriter.close();
        System.out.println("file write Success");


    }

}
