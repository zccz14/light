package com.funcxy.oj.services;

import com.funcxy.oj.models.Passport;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.models.User;
import com.funcxy.oj.utils.InvalidException;
import com.funcxy.oj.utils.Validation;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * Created by aak12 on 2017/2/28.
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public void save(User user) throws InvalidException{
        if (Validation.notValid(user))throw new InvalidException("user input invalid");
        userRepository.save(user);
    }


    public User login(Passport passport){
        if(passport.email==null&&passport.username==null){
            //throw new InvalidException("username or email must be setted");
            return null;
        }
        if(passport.username!=null){
            User userFound = userRepository.findByUsername(passport.username);
            if (userFound.passwordVerify(passport.password))return userFound;
        }else{
            User userFound = userRepository.findByEmail(passport.email);
            if (userFound.passwordVerify(passport.password))return userFound;
        }
        return null;
    }

    public User signUp(@Valid Passport passport){
        if(Validation.isValid(passport)){
            User userFoundByUsername = userRepository.findByUsername(passport.email);
            if (userFoundByUsername!=null)return userFoundByUsername;
            User userFoundByEmail = userRepository.findByEmail(passport.email);
            if (userFoundByEmail!=null)return userFoundByEmail;
            User user = new User();
            user.setUsername(passport.username);
            user.setEmail(passport.email);
            user.setPassword(passport.password);
            return userRepository.insert(user);
        }else {
            System.out.println("passport not valid");
            return null;
        }
    }

    public User findOne(String id){
        if (id==null||id==""){
            System.out.println("userid not setted");
            return null;
        }
        return userRepository.findOne(id);
    }
}
