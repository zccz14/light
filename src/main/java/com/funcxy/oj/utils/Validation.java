package com.funcxy.oj.utils;

import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import static com.funcxy.oj.utils.ComUtil.*;
import com.funcxy.oj.utils.ComUtil;
/**
 * Created by aak1247 on 2017/3/1.
 */

public class Validation {
    @Autowired
    static UserRepository userRepository;
    public static boolean userNotValid(User user){
        String username = user.getUsername().trim();
        if (userRepository.findOneByUsername(username) != null
                || username.length() > Integer.parseInt(ComUtil.properties.getProperty("maximumLengthOfUsername"))) return true;
        if (user.getPassword().length() < Integer.parseInt(ComUtil.properties.getProperty("minimumLengthOfPassword")) ||  user.getPassword().length() > Integer.parseInt(ComUtil.properties.getProperty("maximumLengthOfPassword"))) return true;
        if (!(ComUtil.hasAtLeastXLetters(user.getPassword(), Integer.parseInt(ComUtil.properties.getProperty("minimumLengthOfLettersInPassword")))) || !(ComUtil.hasAtLeastXNumerals(user.getPassword(), Integer.parseInt(ComUtil.properties.getProperty("minimumLengthOfNumeralsInPassword"))))) return true;
        return false;
    }
}
