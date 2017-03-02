package com.funcxy.oj.utils;

import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static com.funcxy.oj.utils.ComUtil.*;

/**
 * Created by aak1247 on 2017/3/1.
 */

public class Validation {
    @Autowired
    static UserRepository userRepository;
    public static boolean userNotValid(User user){
        String username = user.getUsername().trim();
        if (userRepository.findByUsername(username) != null || username.length() > Integer.parseInt(properties.getProperty("maximumLengthOfUsername")) || !isEmail(user.getEmail())) return true;
        if (user.getPassword().length() < Integer.parseInt(properties.getProperty("minimumLengthOfPassword")) ||  user.getPassword().length() > Integer.parseInt(properties.getProperty("maximumLengthOfPassword"))) return true;
        if (!(hasAtLeastXLetters(user.getPassword(), Integer.parseInt(properties.getProperty("minimumLengthOfLettersInPassword")))) || !(hasAtLeastXNumerals(user.getPassword(), Integer.parseInt(properties.getProperty("minimumLengthOfNumeralsInPassword"))))) return true;
        return false;
    }
}
