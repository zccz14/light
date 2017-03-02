package com.funcxy.oj.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ddhee
 */

public class ComUtil {
    static public Properties properties;
    static {
        Properties properties = new Properties();
        try {
            String root = new File("").getAbsolutePath();
            properties.load(new BufferedInputStream(new FileInputStream(root+"\\src\\main\\java\\com\\funcxy\\oj\\Properties.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static public boolean isEmail(String str) {
        String regex = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-Z0-9]-*){1,}\\.){1,3}[a-zA-Z\\-]{1,}";
        return match(regex, str);
    }
    static public boolean hasAtLeastXLetters(String str, int number) {
        String regex = "[a-zA-Z]{" + number + "}";
        return match(regex, str);
    }
    static public boolean hasAtLeastXNumerals(String str, int number) {
        String regex = "\\d{" + number + "}";
        return match(regex, str);
    }
    private static boolean match(String regex, String str){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
