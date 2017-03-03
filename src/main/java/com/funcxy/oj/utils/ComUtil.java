package com.funcxy.oj.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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
            properties.load(new BufferedInputStream(new FileInputStream(root + "\\src\\main\\java\\com\\funcxy\\oj\\Properties.properties")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no such file");
        }
    }
}
