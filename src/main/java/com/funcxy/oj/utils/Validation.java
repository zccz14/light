package com.funcxy.oj.utils;

import com.funcxy.oj.contents.Passport;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;


/**
 * @author aak1247 on 2017/3/1.
 */
public class Validation {
    public static boolean isValid(Passport passport) {
        RegularExpression validUsername = new RegularExpression("^[a-zA-Z0-9_]+");
        RegularExpression validPassword = new RegularExpression("^\\S*([a-zA-Z]+\\S*[0-9]+)|([0-9]+\\S*[A-Za-z]+)+\\S*");
        RegularExpression validEmail = new RegularExpression("^\\S+@[a-zA-Z0-9]+\\.[a-zA-Z]+");
        return validUsername.matches(passport.username) && validPassword.matches(passport.password) && validEmail.matches(passport.email);
    }
}
