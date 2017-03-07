package com.funcxy.oj.controllers.users;

import com.funcxy.oj.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * Created by DDHEE on 2017/3/7.
 */

@RestController
@RequestMapping("/users")
public class UserPrfController {
    @Autowired
    UserRepository userRepository;
    //收藏问题
    @RequestMapping(value = "/liked-problems/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> likedProblems(PathVariable id, HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

    }

    //收藏题单
    //取消收藏问题
    //取消收藏题单
    //获取收藏的题单
    //获取收藏的题目
}
