package com.funcxy.oj.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import static com.sun.org.apache.xerces.internal.impl.dv.util.HexBin.encode;

/**
 * Created by aak1247 on 2017/3/1.
 */
public class UserUtil {
    public static String encrypt(String algorithm, String clearText) {
        try {
            MessageDigest pwd = MessageDigest.getInstance(algorithm);
            pwd.update(clearText.getBytes());
            return encode(pwd.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No Such Algorithm: " + algorithm);
        }
    }
    public static boolean isSignedIn(HttpSession httpSession){
        return httpSession.getAttribute("userId") != null;
    }
    public static String getRandomCharAndNumr(int length) {
        String str = "";
        Random random = new Random();
        for(int i = 0; i < length; i++) {
            boolean b = random.nextBoolean();
            if (b) { // 字符串
                // int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
                str += (char) (65 + random.nextInt(26));// 取得大写字母
            } else { // 数字
                str += String.valueOf(random.nextInt(10));
            }
        }
        return str;
    }
    public static void sendEmail(String email,String randomString){
        Properties projectProps = new Properties();
        Properties mailProps = new Properties();
        Properties i18nProps = new Properties();
        String url = new File("").getAbsolutePath();
        try {
            projectProps.load(new BufferedInputStream(new FileInputStream(url+"\\src\\main\\resources\\project.properties")));
            String language = projectProps.getProperty("lang");
            i18nProps.load(new BufferedInputStream(new FileInputStream(url+"\\src\\main\\resources\\i18n\\"+language+".properties")));
            mailProps.load(new BufferedInputStream(new FileInputStream(url+"\\src\\main\\resources\\mail.properties")));
            Session session = Session.getInstance(mailProps);
            Message msg = new MimeMessage(session);
//            msg.setHeader("Content-Transfer-Encoding", "utf-8");
            sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            msg.setSubject("=?UTF-8?B?"+enc.encode(i18nProps.getProperty("verifyMailHead.subject").getBytes())+"?=");
//            String content = new String(i18nProps.getProperty("verifyMailContent.prefix")+projectProps.getProperty("siteHost")+"users/"+randomString+i18nProps.getProperty("verifyMailContent.suffix"));
            String content = new String(projectProps.getProperty("siteHost")+"users/"+randomString);
            content = new String(content.getBytes("utf-8"),"iso8859-1");
            msg.setContent(content,"text/html;charset=iso8859-1");
            msg.setFrom(new InternetAddress(projectProps.getProperty("mailAccount")));
            msg.setRecipient(Message.RecipientType.TO,new InternetAddress(email));
            msg.setSentDate(new Date());
            Transport transport = session.getTransport();
            transport.connect(projectProps.getProperty("mailAccount"),projectProps.getProperty("mailPassword"));
            transport.sendMessage(msg,msg.getAllRecipients());
            transport.close();
        }catch (Exception e){
            System.out.println("not found");
            return;
        }
    }

    public static void main(String args[]){
        sendEmail("aak1247@126.com","this");
    }
}
