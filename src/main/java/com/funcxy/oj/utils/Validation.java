package com.funcxy.oj.utils;

import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by aak1247 on 2017/3/1.
 */

public class Validation {
    @Autowired
    static UserRepository userRepository;
    public static boolean notValid(User user){
        String username = user.getUsername().trim();
        if(username == null || userRepository.findByUsername(username) != null) return true;
        return false;
    }
}
