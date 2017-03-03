package com.funcxy.oj.controllers;

import com.funcxy.oj.models.Passport;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.InvalidException;
import com.funcxy.oj.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
    public Object login(@RequestBody Passport passport, HttpSession httpSession) throws InvalidException{
        if(passport.email==null&&passport.username==null){
            throw new InvalidException("username or email must be setted");
        }
        if(passport.username!=null){
            User userFound = userRepository.findOneByUsername(passport.username);
            if (userFound.passwordVerify(passport.password)){
                httpSession.setAttribute("userId",userFound.getUserId().toString());
                return userFound;
            }
        }else{
            User userFound = userRepository.findOneByEmail(passport.email);
            if (userFound.passwordVerify(passport.password)){
                httpSession.setAttribute("userId",userFound.getUserId().toString());
                return userFound;
            }
        }
        return new InvalidException("user not found");
    }

    @RequestMapping(value = "/signup", method = POST)//注册
    public User signup(@RequestBody Passport passport, HttpSession httpSession) throws InvalidException{
//        System.out.println(passport.username+passport.email+passport.password);
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
        //TODO: handle with the
        String id = new String(httpSession.getAttribute("userId").toString());
        if (id==null||id==""){
            System.out.println("userid not setted");
            throw new InvalidException("userid not setted");
        }else return userRepository.findOne(id);
    }
}
