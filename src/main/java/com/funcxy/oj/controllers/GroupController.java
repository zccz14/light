package com.funcxy.oj.controllers;


import com.funcxy.oj.errors.FieldsDuplicateError;
import com.funcxy.oj.errors.FieldsInvalidError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.Group;
import com.funcxy.oj.models.GroupType;
import com.funcxy.oj.models.Message;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.GroupRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author aak1247 on 2017/3/4.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {
    final
    UserRepository userRepository;
    final
    GroupRepository groupRepository;

    @Autowired
    public GroupController(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }


    //创建群组
    @RequestMapping(value = "/create", method = POST)
    public ResponseEntity<Object> createGroup(@RequestBody @Valid Group group, HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        if (groupRepository.findOneByGroupName(group.getGroupName()) != null)
            return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
        user.addGroupIn(group.getId());
        groupRepository.save(group);
        userRepository.save(user);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }
    //解散群组

    @RequestMapping(value = "/{groupName}/dismiss", method = POST)//解散群组
    public ResponseEntity dismissGroup(@RequestBody DismissVerification dismissVerification, @PathVariable String groupName, HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        Group group = groupRepository.findById(dismissVerification.groupId);
        if (!user.getId().equals(group.getOwnerId()))
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        if (user == null || group == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        if (!user.passwordVerify(dismissVerification.password)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        if (!group.getGroupName().equals(dismissVerification.name))
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        user.deleteGroupIn(group.getId());
        groupRepository.delete(group);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //搜索群组
    @RequestMapping(value = "/search", method = GET)
    public ResponseEntity searchGroup(@RequestParam(defaultValue = "/*") String groupName,
                                      @RequestParam(defaultValue = "/*") String type,
                                      Pageable pageable) {
        Page<Group> groups = groupRepository.roughFind(groupName, type, pageable);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    // 查看群组资料
    @RequestMapping(value = "/{groupName}", method = GET)
    public ResponseEntity getGroup(@PathVariable String groupName, HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    //修改群组资料
    @RequestMapping(value = "/{groupName}/profile", method = PUT)
    public ResponseEntity updateGroup(@PathVariable String groupName,
                                      @RequestBody Group group,
                                      HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group groupFound = groupRepository.findOneByGroupName(groupName);
        if (!groupFound.getOwnerId().equals(userRepository.findById(httpSession.getAttribute("userId").toString()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
//        if (group.getOwnerId()!=null)groupFound.setOwnerId(group.getOwnerId());
        if (group.getGroupName() != null) {
            if (groupRepository.findOneByGroupName(group.getGroupName()) != null) {
                return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
            }
            groupFound.setGroupName(group.getGroupName());
        }
        if (group.getType() != null) {
            groupFound.setType(group.getType());
        }
        groupRepository.save(groupFound);
        return new ResponseEntity<>(groupFound, HttpStatus.OK);
    }

    @RequestMapping(value = "/{groupName}/alienate", method = PUT)
    public ResponseEntity alienate(@PathVariable String groupName,
                                   @RequestBody InnerClassUser owner,
                                   HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        if (!group.getOwnerId().equals(httpSession.getAttribute("userId").toString())) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        group.setOwnerId(httpSession.getAttribute("userId").toString());
        groupRepository.save(group);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    //申请加入
    @RequestMapping(value = "/{groupName}/apply-for", method = POST)
    public ResponseEntity applyFor(@PathVariable String groupName,
                                   @RequestBody User user,
                                   HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }

        if (group.getMemberId().contains(user.getId()) || group.getInvitedMemberId().contains(user.getId())) {
            return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
        }

        if (group.getType().equals(GroupType.FREE)) {
            group.addMember(user.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        group.askJoin(user.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //邀请成员
    @RequestMapping(value = "{groupName}/invite", method = POST)
    public ResponseEntity invite(@PathVariable String groupName,
                                 @RequestBody User user,
                                 HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        User userFound = userRepository.findById(user.getId());
        if (userFound == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        if (group == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        group.inviteMember(user.getId());
        userFound.addMessage(new Message("Invitation", "you are invited to " + group.getGroupName(), 2));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{groupName}/manage", method = POST)
    public ResponseEntity handleApply(@PathVariable String groupName,
                                      @RequestBody InnerClassReport report,
                                      HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        if (!group.getOwnerId().equals(user.getId())) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        if (report.result.equals("admit")) {
            user.addGroupIn(group.getId());
            if (user.getGroupIn().contains(group.getId())) {
                return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
            } else {
                user.addGroupIn(group.getId());
                group.addMember(user.getId());
                userRepository.save(user);
                groupRepository.save(group);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else if (report.result.equals("refuse")) {
            group.refuse(user.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
    }

    //劝退成员
    @RequestMapping(value = "/{groupName}/manage", method = DELETE)
    public ResponseEntity deleteMember(@PathVariable String groupName,
                                       @RequestBody InnerClassUser user,
                                       HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User userFound = userRepository.findById(user.userId);
        Group group = groupRepository.findOneByGroupName(groupName);
        if (!group.getOwnerId().equals(httpSession.getAttribute("userId").toString())) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        if (group.getMemberId().remove(user.userId)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
    }

    //获取群组成员列表
    @RequestMapping(value = "/{groupName}/members", method = GET)
    public ResponseEntity retriveMember(@PathVariable String groupName,
                                        Pageable pageable,
                                        HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        if (group.getType().equals(GroupType.CLOSE)) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(
                    new PageImpl<>(group.getMemberId()
                            .stream()
                            .map(
                                    mem -> userRepository.findById(mem)
                            )
                            .collect(Collectors.toList()),
                            pageable,
                            group.getMemberId().size()),
                    HttpStatus.OK);
        }
    }

    class DismissVerification {
        String groupId;
        String name;
        String password;

        DismissVerification(String groupId, String name, String password) {
            this.groupId = groupId;
            this.name = name;
            this.password = password;
        }
    }

    //转让群组
    class InnerClassUser {
        public String userId;

        public InnerClassUser(String objectId) {
            this.userId = objectId;
        }
    }

    //同意/拒绝加入(群组管理员视角) 仅针对请求加入的情况
    class InnerClassReport {
        String result;

        /**
         * @param result admit/refuse
         */
        public InnerClassReport(String result) {
            this.result = result;
        }
    }
}
