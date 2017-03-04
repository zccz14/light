package com.funcxy.oj.controllers;

import com.funcxy.oj.models.Passport;
import com.funcxy.oj.models.Profile;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.InvalidException;
import com.funcxy.oj.utils.Validation;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * User Controller
 * Created by zccz14 on 2017/3/2.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login", method = POST)//登录
    public ResponseEntity<Object> login(@RequestBody Passport passport, HttpSession httpSession) throws InvalidException{
        if(passport.username==null){
//            throw new InvalidException("username or email must be setted");
            return new ResponseEntity<>(new InvalidException("username or email must be setted"),HttpStatus.BAD_REQUEST);
        }else{
            System.out.println(passport.username+"login");
            RegularExpression regExpEmail = new RegularExpression("^\\S+@[a-zA-Z0-9]+\\.[a-zA-Z]+");
            RegularExpression regExpUsername = new RegularExpression("^[a-zA-Z0-9_]+");
            if (regExpEmail.matches(passport.username)){
                User userFound = userRepository.findOneByEmail(passport.username);
                System.out.println(userFound);
                if (userFound.passwordVerify(passport.password)){
                    httpSession.setAttribute("userId",userFound.getUserId().toString());
                    return new ResponseEntity<>(userFound, HttpStatus.OK);
                }
            }else  if(regExpUsername.matches(passport.username)){
                User userFound = userRepository.findOneByUsername(passport.username);
                System.out.println("found by username"+userFound);
                if (userFound.passwordVerify(passport.password)){
                    httpSession.setAttribute("userId",userFound.getUserId().toString());
                    return new ResponseEntity<>(userFound,HttpStatus.OK);
                }
                System.out.println("password wrong"+passport.password);
                return new ResponseEntity<>(new InvalidException("wrong password"), HttpStatus.FORBIDDEN);
            }else {
//                throw new InvalidException("input illegal");
                return new ResponseEntity<>(new InvalidException("input illegal"), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(new InvalidException("user not found"), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/signup", method = POST)//注册
    public ResponseEntity<Object> signup(@RequestBody @Valid Passport passport, HttpSession httpSession) throws InvalidException{
        System.out.println(passport.username+passport.email+passport.password);
        if(Validation.isValid(passport)){
            System.out.println(passport.username+" sign up");
            User userFoundByUsername = userRepository.findOneByUsername(passport.email);
            if (userFoundByUsername!=null){
                return new ResponseEntity<>(new InvalidException("username existed"), HttpStatus.BAD_REQUEST);
            }
            User userFoundByEmail = userRepository.findOneByEmail(passport.email);
            if (userFoundByEmail!=null) {
                return new ResponseEntity<>(new InvalidException("email existed"), HttpStatus.BAD_REQUEST);
            }
            User user = new User();
            user.setUsername(passport.username);
            user.setEmail(passport.email);
            user.setPassword(passport.password);
            return new ResponseEntity<>(userRepository.insert(user), HttpStatus.CREATED);
        }else {
            System.out.println("invalid passport");
            return new ResponseEntity<>(new InvalidException("invalid input"), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/profile", method = GET)//获取详细资料
    public ResponseEntity<Object> profile(HttpSession httpSession) throws InvalidException{
        HttpHeaders responseHeaders = new HttpHeaders();
        String id = new String(httpSession.getAttribute("userId").toString());
        if (id==null||id==""){
            System.out.println("userid not setted");
            return new ResponseEntity<>(new InvalidException("haven't signed in yet"), HttpStatus.FORBIDDEN);
        }else return new ResponseEntity<>(userRepository.findOne(id), responseHeaders, HttpStatus.FOUND);
    }

    @RequestMapping(value = "/profile",method = PUT)
    public User putProfile(@RequestBody @Valid Profile profile,HttpSession httpSession){
        return null;
    }
}
