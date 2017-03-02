package com.funcxy.oj.controllers;

import com.funcxy.oj.Application;
import com.funcxy.oj.models.User;
import com.funcxy.oj.services.UserService;
import com.funcxy.oj.utils.InvalidException;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by aak12 on 2017/3/1.
 */

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@SpringBootConfiguration
@SpringBootTest(classes = Application.class)
public class UserTest{
    static User user;
    static String usernameValid = "zccz14";
    static String usernameDuplicated = "  z ccz 1  4 ";
    static String usernameEmpty = "      ";
    static String emailValid = "hell@funcxy.com";
    static String emailEmpty = "";
    static String emailInvalid = "asfdjklas@.com";
    static String passwordValid = "6789067890";
    static String passwordEmpty = "";
    static String passwordInvalid = "";
    @Autowired
    static UserService userService;

    @Before
    public static void validUser() throws InvalidException{
        user.setUsername(usernameValid);
        user.setEmail(emailValid);
        user.setPassword(passwordValid);
    }

    // All valid
    @Test
    public static void signUpTest0() throws InvalidException{
        userService.save(user);
    }

    // Duplicated username
    @Test(expected = InvalidException.class)
    public void signUpTest1() throws InvalidException{
        userService.save(user);
        userService.save(user);
    }
    // Duplicated username
    @Test(expected = InvalidException.class)
    public static void signUpTest2() throws InvalidException{
        userService.save(user);
        user.setUsername(usernameDuplicated);
        userService.save(user);
    }
    // Empty username
    @Test(expected = InvalidException.class)
    public static void signUpTest3() throws InvalidException{
        user.setUsername(usernameEmpty);
        userService.save(user);
    }

    // Empty email
    @Test(expected = InvalidException.class)
    public static void signUpTest4() throws InvalidException{
        user.setEmail(emailEmpty);
        userService.save(user);
    }
    // Invalid email
    @Test(expected = InvalidException.class)
    public static void signUpTest5() throws InvalidException{
        user.setEmail(emailInvalid);
        userService.save(user);
    }

    // Empty password
    @Test(expected = InvalidException.class)
    public static void signUpTest6() throws InvalidException{
        user.setPassword(passwordEmpty);
        userService.save(user);
    }
    // Invalid password
    @Test(expected = InvalidException.class)
    public static void signUpTest7() throws InvalidException{
        user.setPassword(passwordInvalid);
        userService.save(user);
    }

}
