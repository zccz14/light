package com.funcxy.oj.controllers;

import com.funcxy.oj.models.Passport;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.InvalidException;
import com.funcxy.oj.utils.Validation;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * User Controller
 * Created by zccz14 on 2017/3/2.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = POST)//登录
    public User login(@RequestBody Passport passport, HttpSession httpSession) throws InvalidException{
        if(passport.username==null){
            throw new InvalidException("username or email must be setted");
        }else{
            System.out.println(passport.username+"login");
            RegularExpression regExpEamil = new RegularExpression("^\\S+@[a-zA-Z0-9]+\\.[a-zA-Z]+");
            RegularExpression regExpUsername = new RegularExpression("^[a-zA-Z0-9_]+");
            if (regExpEamil.matches(passport.username)){
                User userFound = userRepository.findOneByEmail(passport.username);
                System.out.println(userFound);
                if (userFound.passwordVerify(passport.password)){
                    httpSession.setAttribute("userId",userFound.getUserId().toString());
                    return userFound;
                }
            }else  if(regExpUsername.matches(passport.username)){
                User userFound = userRepository.findOneByUsername(passport.username);
                System.out.println("found by username"+userFound);
                if (userFound.passwordVerify(passport.password)){
                    httpSession.setAttribute("userId",userFound.getUserId().toString());
                    return userFound;
                }
                System.out.println("password wrong"+passport.password);
            }else {
                throw new InvalidException("input illegal");
            }
        }

        throw  new InvalidException("user password not collect or user not found");
    }

    @RequestMapping(value = "/signup", method = POST)//注册
    public User signup(@RequestBody Passport passport, HttpSession httpSession) throws InvalidException{
        System.out.println(passport.username+passport.email+passport.password);
        if(Validation.isValid(passport)){
            System.out.println(passport.username+"signup");
            User userFoundByUsername = userRepository.findOneByUsername(passport.email);
            if (userFoundByUsername!=null){
                throw new InvalidException("already signup");
            }
            User userFoundByEmail = userRepository.findOneByEmail(passport.email);
            if (userFoundByEmail!=null) {
                throw new InvalidException("already signup");
            }
            User user = new User();
            user.setUsername(passport.username);
            user.setEmail(passport.email);
            user.setPassword(passport.password);
            return userRepository.insert(user);
        }else {
            System.out.println("passport not valid");
            throw new InvalidException("input not valid");
        }

    }

    @RequestMapping(value = "/profile", method = GET)//获取详细资料
    public User profile(HttpSession httpSession) throws InvalidException{
        String id = new String(httpSession.getAttribute("userId").toString());
        if (id==null||id==""){
            System.out.println("userid not setted");
            throw new InvalidException("userid not setted");
        }else return userRepository.findOne(id);
    }
}
