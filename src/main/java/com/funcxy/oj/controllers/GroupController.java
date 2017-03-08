package com.funcxy.oj.controllers;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by aak1247 on 2017/3/4.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {
    @RequestMapping(value = "/create",method = POST)
    public ResponseEntity<Object> createGroup(){

        return null;
    }
    //TODO:创建群组
    //TODO:解散群组
    //TODO:查看群组资料
    //TODO:修改群组资料
    //TODO:搜索群组
    //TODO:创建题单
    //TODO:删除题单
    //TODO:获取题单列表
    //TODO:转让群组
    //TODO:同意加入
    //TODO:拒绝加入
    //TODO:邀请成员
    //TODO:申请加入
    //TODO:获取群组成员列表
}
