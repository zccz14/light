package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.models.Group;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.UserUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by aak1247 on 2017/3/4.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    UserRepository userRepository;
    //创建群组
    @RequestMapping(value = "/create",method = POST)
    public ResponseEntity<Object> createGroup(@RequestBody @Valid Group group, HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));

        return null;
    }
    //TODO: 解散群组
    //TODO: 查看群组资料
    //TODO: 修改群组资料
    //TODO: 搜索群组
    //TODO: 转让群组
    //TODO: 同意加入
    //TODO: 拒绝加入
    //TODO: 邀请成员
    //TODO: 申请加入
    //TODO: 获取群组成员列表
}
