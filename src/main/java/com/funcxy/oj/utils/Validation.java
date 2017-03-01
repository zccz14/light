package com.funcxy.oj.utils;

import com.funcxy.oj.models.User;


/**
 * Created by aak1247 on 2017/3/1.
 */
public class Validation {
    public static boolean notValid(User user){
        if (user.getUsername()== null)return true;
        return false;
    }
}
