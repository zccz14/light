package com.funcxy.oj.controllers;

import com.funcxy.oj.Application;
import com.funcxy.oj.contents.Passport;
import com.funcxy.oj.models.User;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.web.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by DDHEE on 2017/3/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@SpringBootTest(classes = Application.class)
public class UserSignTest {
    static User user = new User();
    static Passport passport = new Passport();
    static String usernameValid = "zccz14";
    static String usernameDuplicated = "  z ccz 1  4 ";
    static String usernameEmpty = "      ";
    static String emailValid = "hell@funcxy.com";
    static String emailEmpty = "";
    static String emailInvalid = "asfdjklas@.com";
    static String passwordValid = "abc6789067890";
    static String passwordEmpty = "";
    static String passwordInvalid = "243";

    @Before
    public void validUser () throws Exception {
        passport.username = usernameValid;
        passport.email = emailValid;
        passport.password = passwordValid;
    }

    // All valid
    @Test
    public void signUpTest0 () throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Passport> httpEntity = new HttpEntity<>(passport);
        try {
            ResponseEntity<Passport> responseEntity = restTemplate.postForEntity(new URI("http://localhost:8080/users/sign-up"), httpEntity, Passport.class);
            System.out.println(responseEntity.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

//    // Duplicated username
//    @Test(expected = Exception.class)
//    public void signUpTest1() throws Exception{
//        userService.save(user);
//        userService.save(user);
//    }
//    // Duplicated username
//    @Test(expected = Exception.class)
//    public void signUpTest2() throws Exception{
//        userService.save(user);
//        user.setUsername(usernameDuplicated);
//        userService.save(user);
//    }
//    // Empty username
//    @Test(expected = Exception.class)
//    public void signUpTest3() throws Exception{
//        user.setUsername(usernameEmpty);
//        userService.save(user);
//    }

//    // Empty email
//    @Test(expected = Exception.class)
//    public void signUpTest4() throws Exception{
//        user.setEmail(emailEmpty);
//        userService.save(user);
//    }
//    // Invalid email
//    @Test(expected = Exception.class)
//    public void signUpTest5() throws Exception{
//        user.setEmail(emailInvalid);
//        userService.save(user);
//    }

//    // Empty password
//    @Test(expected = Exception.class)
//    public void signUpTest6() throws Exception{
//        user.setPassword(passwordEmpty);
//        userService.save(user);
//    }
//    // Invalid password
//    @Test(expected = Exception.class)
//    public void signUpTest7() throws Exception{
//        user.setPassword(passwordInvalid);
//        userService.save(user);
//    }

}
