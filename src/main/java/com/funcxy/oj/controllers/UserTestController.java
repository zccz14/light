package com.funcxy.oj.controllers;

import com.funcxy.oj.modules.User;
import com.funcxy.oj.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by DDHEE on 2017/2/28.
 */
@RestController
@RequestMapping("/user")
public class UserTestController {
    @Autowired
    UserRepository userRepository;



    @RequestMapping(method = RequestMethod.POST)
    User signUp (@RequestBody User user) {
        return userRepository.save(user);
    }

    @RequestMapping(method = RequestMethod.GET)
    List<User> list () {
        return userRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/username")
    List<User> exactQueryByUsername (@PathVariable String username) {
        return userRepository.findByUsername(username);
    }
    /*

    @RequestMapping(method = RequestMethod.GET, value = "/username/problemLiked")
    List<ObjectId> problemLiked (@PathVariable String username) {
        List<User> a;
        a = userRepository.findByUsername(username);
        return a.get(0).getProblemLiked();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/username/likeProblem/problemId")
    boolean likeProblem (@PathVariable String username, @PathVariable String problemId) {
        List<User> a;
        a = userRepository.findByUsername(username);
        a.get(0).set
    }

    */

}
