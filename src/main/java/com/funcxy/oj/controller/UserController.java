package com.funcxy.oj.controller;

import com.funcxy.oj.dao.UserDao;
import com.funcxy.oj.model.User;
import com.funcxy.oj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.System.out;

/**
 * Created by aak12 on 2017/2/28.
 */
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    public String index(){
        return "hello";
    }
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public void signup(@RequestParam String username,String password,String email){
        User user=new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        userService.save(user);
        System.out.println(userDao.findAll());
    }
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String find(@RequestParam String username){
        return userDao.findByUsername(username).toString();
    }
}
