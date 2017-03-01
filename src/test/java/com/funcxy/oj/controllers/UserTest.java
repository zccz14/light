package com.funcxy.oj.controllers;

import com.funcxy.oj.Application;
import com.funcxy.oj.models.User;
import com.funcxy.oj.service.UserService;
import com.funcxy.oj.utils.InvalidException;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by aak12 on 2017/3/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@SpringBootConfiguration
@SpringBootTest(classes = Application.class)
public class UserTest{
    @Autowired
    static UserService userService;
    @BeforeClass
    public static void createUser() throws InvalidException{
        User user = new User();
        user.setUsername("aak1247");
        user.setEmail("fuck@124.com");
        user.setPassword("123456");
        userService.save(user);
    }
    @Test(expected = InvalidException.class)//no username and password
    public void signupTest1()throws InvalidException{
        User user = new User();
        user.setId(ObjectId.get());
        userService.save(user);
    }
}
