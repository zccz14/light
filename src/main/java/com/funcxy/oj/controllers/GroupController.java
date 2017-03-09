package com.funcxy.oj.controllers;


import com.funcxy.oj.errors.FieldsDuplicateError;
import com.funcxy.oj.errors.FieldsInvalidError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.Group;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.GroupRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.UserUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author  aak1247 on 2017/3/4.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GroupRepository groupRepository;
    //创建群组
    @RequestMapping(value = "/create",method = POST)
    public ResponseEntity<Object> createGroup(@RequestBody @Valid Group group, HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
        if (groupRepository.findOneByGroupName(group.getGroupName())!=null)return new ResponseEntity<Object>(new FieldsDuplicateError(),HttpStatus.BAD_REQUEST);
        user.addGroupIn(group.getId());
        groupRepository.save(group);
        userRepository.save(user);
        return new ResponseEntity<Object>(group,HttpStatus.CREATED);
    }
    //解散群组

    class DismissVerification {
        ObjectId groupId;
        String name;
        String password;
        DismissVerification(ObjectId groupId,String name,String password){
            this.groupId = groupId;
            this.name = name;
            this.password = password;
        }
    }

    @RequestMapping(value = "/{groupName}/dismiss",method = POST)//解散群组
    public ResponseEntity dismissGroup(@RequestBody DismissVerification dismissVerification,@PathVariable String groupName, HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
        Group group = groupRepository.findById(dismissVerification.groupId);
        if (!user.getId().equals(group.getOwnerId()))return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        if (user == null||group == null) return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        if (!user.passwordVerify(dismissVerification.password)){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        if (!group.getGroupName().equals(dismissVerification.name)) return new ResponseEntity<>(new FieldsInvalidError(),HttpStatus.BAD_REQUEST);
        user.deleteGroupIn(group.getId());
        groupRepository.delete(group);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //TODO: 搜索群组
    // 查看群组资料
    @RequestMapping(value = "/{groupName}",method = GET)
    public ResponseEntity getGroup(@PathVariable String groupName,HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null)return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(group,HttpStatus.OK);
    }
    //TODO: 修改群组资料
    //TODO: 转让群组
    //TODO: 同意加入
    //TODO: 拒绝加入
    //TODO: 邀请成员
    //TODO: 申请加入
    //TODO: 获取群组成员列表
}
