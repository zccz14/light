package com.funcxy.oj.controllers;

import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.models.User;
import com.funcxy.oj.services.UserService;
import com.funcxy.oj.utils.InvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by aak1247 on 2017/2/28.
 */
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    public String index(){
        return "hello";
    }

    //注册
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public void signup(@RequestParam User user) throws InvalidException{
//        User user=new User();
//        user.setUsername(username);
//        user.setPassword(password);
//        user.setEmail(email);
        userService.save(user);
        System.out.println(userRepository.findAll());
    }
    //查找用户
    @RequestMapping(value = "/user/{username}",method = RequestMethod.GET)
    public String find(@RequestParam String username){
        return userRepository.findByUsername(username).toString();
    }
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String signin(@RequestParam User user){
        return "success";
    }
}
