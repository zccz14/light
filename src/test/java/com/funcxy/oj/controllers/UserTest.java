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
    User user;
    String usernameValid = "zccz14";
    String usernameDuplicated = "  z ccz 1  4 ";
    String usernameEmpty = "      ";
    String emailValid = "hell@funcxy.com";
    String emailEmpty = "";
    String emailInvalid = "asfdjklas@.com";
    String passwordValid = "6789067890";
    String passwordEmpty = "";
    String passwordInvalid = "243";
    @Autowired
    UserService userService;

    @Before
    public void validUser() throws InvalidException{
        user.setUsername(usernameValid);
        user.setEmail(emailValid);
        user.setPassword(passwordValid);
    }

    // All valid
    @Test
    public void signUpTest0() throws InvalidException{
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
    public void signUpTest2() throws InvalidException{
        userService.save(user);
        user.setUsername(usernameDuplicated);
        userService.save(user);
    }
    // Empty username
    @Test(expected = Exception.class)
    public void signUpTest3() throws InvalidException{
        user.setUsername(usernameEmpty);
        userService.save(user);
    }

    // Empty email
    @Test(expected = Exception.class)
    public void signUpTest4() throws InvalidException{
        user.setEmail(emailEmpty);
        userService.save(user);
    }
    // Invalid email
    @Test(expected = Exception.class)
    public void signUpTest5() throws InvalidException{
        user.setEmail(emailInvalid);
        userService.save(user);
    }

    // Empty password
    @Test(expected = Exception.class)
    public void signUpTest6() throws InvalidException{
        user.setPassword(passwordEmpty);
        userService.save(user);
    }
    // Invalid password
    @Test(expected = InvalidException.class)
    public void signUpTest7() throws InvalidException{
        user.setPassword(passwordInvalid);
        userService.save(user);
    }

}
