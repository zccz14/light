package com.funcxy.oj.utils;

import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import static com.funcxy.oj.utils.ComUtil.*;
import com.funcxy.oj.utils.ComUtil;
import org.springframework.stereotype.Service;

/**
 * Created by aak1247 on 2017/3/1.
 */
@Service
public class Validation {
    @Autowired
    UserRepository userRepository;
    public boolean userNotValid(User user){
        String username = user.getUsername().trim();
        if (username.length() <= Integer.parseInt(ComUtil.properties.getProperty("maximumLengthOfUsername"))
                && user.getPassword().length() >= Integer.parseInt(ComUtil.properties.getProperty("minimumLengthOfPassword"))
                && user.getPassword().length() <= Integer.parseInt(ComUtil.properties.getProperty("maximumLengthOfPassword"))
                && ComUtil.hasAtLeastXLetters(user.getPassword())
                && ComUtil.hasAtLeastXNumerals(user.getPassword())) return false;
        return true;
    }
}
