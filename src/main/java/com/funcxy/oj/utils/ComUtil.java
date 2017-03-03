package com.funcxy.oj.utils;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

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
        properties = new Properties();
        System.out.println("class loaded");
        try {
            String root = new File("").getAbsolutePath();
            properties.load(new BufferedInputStream(new FileInputStream(root+"\\src\\main\\java\\com\\funcxy\\oj\\Properties.properties")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no such file");
        }
    }
    static public boolean isEmail(String str) {
        String regex = "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-Z0-9]-*){1,}\\.){1,3}[a-zA-Z\\-]{1,}";
        return match(regex, str);
    }
    static public boolean hasAtLeastXLetters(String str) {
        String regex = "[a-zA-Z]+";
        System.out.println(str+match(regex,str));
        return match(regex, str);
    }
    static public boolean hasAtLeastXNumerals(String str) {
        String regex = "\\d+";
        System.out.println(str+match(regex,str));
        return match(regex, str);
    }
    private static boolean match(String regex, String str){
        RegularExpression regularExpression = new RegularExpression(regex);
        return regularExpression.matches(str);
    }
}
