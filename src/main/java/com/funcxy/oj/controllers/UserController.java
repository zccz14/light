package com.funcxy.oj.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.funcxy.oj.models.Passport;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.services.UserService;
import com.funcxy.oj.utils.InvalidException;
import com.mongodb.util.JSON;
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
    private UserService userService;

    @RequestMapping(value = "/login", method = POST)
    public Object login(@RequestBody Passport passport, HttpSession httpSession) {
        User user = userService.login(passport);
        if (user != null){
            httpSession.setAttribute("userId",user.getId().toString());
            return user;
        }
        else return null;
    }

    @RequestMapping(value = "/signup", method = POST)
    public User signup(@RequestBody Passport passport, HttpSession httpSession) throws InvalidException{
        System.out.println(passport.username+"signup");
        return userService.signUp(passport);
    }

    @RequestMapping(value = "/profile", method = GET)
    public User profile(HttpSession httpSession) {
        //TODO: handle with the
        return userService.findOne(httpSession.getAttribute("userId").toString());
    }
}
