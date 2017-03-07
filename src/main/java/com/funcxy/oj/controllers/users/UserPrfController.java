package com.funcxy.oj.controllers.users;

import com.funcxy.oj.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by DDHEE on 2017/3/7.
 */

@RestController
@RequestMapping("/users")
public class UserPrfController {
    @Autowired
    UserRepository userRepository;
    //收藏问题
    //收藏题单
    //取消收藏问题
    //取消收藏题单
    //获取收藏的题单
    //获取收藏的题目
}
