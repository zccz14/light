package com.funcxy.oj.controllers;

import com.funcxy.oj.models.Passport;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
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
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/login", method = POST)
    public User login(@RequestBody Passport passport, HttpSession httpSession) {
        System.out.println(passport.username);
        System.out.println(passport.password);
        User user = userRepository.findOneByUsername(passport.username);
        if (user == null) {
            System.out.println("no such user");
            // TODO: Change Response
            return null;
        } else if (user.passwordVerify(passport.password)) {
            System.out.println("user login");
            System.out.println(user.getUsername());
            httpSession.setAttribute("userId", user.getUserId().toString());
            return user;
        } else {
            System.out.println("wrong username or password");
            // TODO: Change Response
            return null;
        }
    }

    @RequestMapping(value = "/signup", method = POST)
    public User signup(@RequestBody User user, HttpSession httpSession) {
        return userRepository.insert(user);
    }

    @RequestMapping(value = "/profile", method = GET)
    public User profile(HttpSession httpSession) {
        String userId = (String) httpSession.getAttribute("userId");
        if (userId == null) {
            System.out.println("not login yet");
            return null;
        }
        return userRepository.findOne(userId);
    }
}
