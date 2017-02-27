package com.funcxy.oj.service;

import com.funcxy.oj.dao.UserDao;
import com.funcxy.oj.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by aak12 on 2017/2/28.
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;
    public void save(User user){
        final String algorithm = "SHA1";
        try{
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(user.getPassword().getBytes());
            user.setPassword(messageDigest.digest().toString());
            userDao.save(user);
        }catch(NoSuchAlgorithmException exception){
            System.out.print("no such algorithm");
        }
    }
}
