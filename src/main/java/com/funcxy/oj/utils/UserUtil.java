package com.funcxy.oj.utils;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by aak1247 on 2017/3/1.
 */
public class UserUtil {
    public static String encrypt(String algorithm, String clearText) {
        try {
            MessageDigest pwd = MessageDigest.getInstance(algorithm);
            pwd.update(clearText.getBytes());
            return HexBin.encode(pwd.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No Such Algorithm: " + algorithm);
        }
    }
    public static boolean access(HttpSession httpSession){
        ObjectId userId = (ObjectId) httpSession.getAttribute("userId");
        return ObjectId.isValid(userId.toHexString());
    }
}
