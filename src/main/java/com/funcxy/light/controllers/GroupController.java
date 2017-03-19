package com.funcxy.light.controllers;


import com.funcxy.light.contents.BindingProblemLists;
import com.funcxy.light.contents.UserHeader;
import com.funcxy.light.errors.*;
import com.funcxy.light.models.*;
import com.funcxy.light.repositories.GroupRepository;
import com.funcxy.light.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.funcxy.light.utils.UserUtil.isSignedIn;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * @author aak1247 on 2017/3/4.
 * @author Peter
 */
@RestController
@RequestMapping("/groups")
public class GroupController {
    /**
     * 用户数据库操作
     */
    private final
    UserRepository userRepository;

    /**
     * 用户组数据库操作
     */
    private final
    GroupRepository groupRepository;

    /**
     * 构造函数
     *
     * @param userRepository  用户仓库
     * @param groupRepository 用户组仓库
     */
    @Autowired
    public GroupController(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }


    /**
     * POST创建群组
     *
     * @param group 前端传回有效的Group数据
     * @param httpSession 请求会话
     * @return 是否创建成功
     */
    @RequestMapping(value = "/create", method = POST)
    public ResponseEntity<Object> createGroup(@RequestBody @Valid Group group, HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
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

    /**
     * POST解散群组
     *
     * @param dismissVerification 解散群组的权限测试
     * @param groupName 组名
     * @param httpSession 请求会话
     * @return 是否解散成功
     */
    @RequestMapping(value = "/{groupName}/dismiss", method = POST)//解散群组
    public ResponseEntity dismissGroup(@RequestBody DismissVerification dismissVerification, @PathVariable String groupName, HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
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

    /**
     * GET搜索群组
     *
     * @param groupName 搜索的群组名
     * @param type 搜索的群组类型
     * @see GroupType
     * @param pageable 搜索结果的分页参数
     * @see Pageable
     * @return 搜索结果
     */
    @RequestMapping(value = "/search", method = GET)
    public ResponseEntity searchGroup(@RequestParam(defaultValue = "/*") String groupName,
                                      @RequestParam(defaultValue = "/*") String type,
                                      Pageable pageable) {
        Page<Group> groups = groupRepository.roughFind(groupName, type, pageable);
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    /**
     * GET查看某一群组详细信息
     *
     * @param groupName 群组名
     * @param httpSession 请求会话
     * @return 查询结果
     */
    @RequestMapping(value = "/{groupName}", method = GET)
    public ResponseEntity getGroup(@PathVariable String groupName, HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(group, HttpStatus.OK);
    }

    /**
     * PUT修改群组资料
     *
     * @param groupName 待修改的群组名
     * @param group 修改后的群组信息
     * @param httpSession 请求会话
     * @return 修改后的群组
     */
    @RequestMapping(value = "/{groupName}/profile", method = PUT)
    public ResponseEntity updateGroup(@PathVariable String groupName,
                                      @RequestBody Group group,
                                      HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group groupFound = groupRepository.findOneByGroupName(groupName);
        if (!groupFound.getOwnerId().equals(userRepository.findById(httpSession.getAttribute("userId").toString()).getId())) {
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

    /**
     * alienate 转让群组
     *
     * @param groupName   群组名
     * @param owner       下一任群主
     * @param httpSession session
     * @return 成功时return修改后的群组
     */
    @RequestMapping(value = "/{groupName}/alienate", method = PUT)
    public ResponseEntity alienate(@PathVariable String groupName,
                                   @RequestBody InnerClassUser owner,
                                   HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
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

    /**
     * POST申请加入群组
     *
     * @param groupName 所申请的群组名
     * @param user 进行申请的用户
     *             @see User
     * @param httpSession 请求会话
     * @return 无
     */
    @RequestMapping(value = "/{groupName}/apply-for", method = POST)
    public ResponseEntity applyFor(@PathVariable String groupName,
                                   @RequestBody User user,
                                   HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }

        if (group.getMemberId().contains(user.getId()) || group.getInvitedMemberId().contains(user.getId())) {
            return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
        }

        switch (group.getType()) {
            case CLOSE:
                return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
            case FREE:
                group.addMember(user.getId());
                return new ResponseEntity<>(HttpStatus.OK);
            case OPEN:
                group.askJoin(user.getId());
                return new ResponseEntity<>(HttpStatus.OK);
            default:
                return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * POST邀请成员
     *
     * @param groupName 进行邀请的组的名称
     * @param user 被邀请的用户
     *             @see User
     * @param httpSession
     * @return 无
     */
    @RequestMapping(value = "{groupName}/invite", method = POST)
    public ResponseEntity invite(@PathVariable String groupName,
                                 @RequestBody User user,
                                 HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        User userFound = userRepository.findById(user.getId());
        if (userFound == null || group == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        group.inviteMember(user.getId());
        String tempGroupId = group.getId();
        userFound.addMessage(new Message("Invitation", "You are invited to " + group.getGroupName(), MessageType.INVITATION, tempGroupId));
        if (!userFound.getInvitation().contains(tempGroupId)) {
            userFound.getInvitation().add(tempGroupId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
    }

    /**
     * 同意/拒绝加入(群组管理员视角) 仅针对请求加入的情况
     *
     * @param groupName   群组名称
     * @param report      处理结果
     * @param httpSession session
     * @return 成功时返回OK
     */
    @RequestMapping(value = "/{groupName}/manage", method = POST)
    public ResponseEntity handleApply(@PathVariable String groupName,
                                      @RequestBody InnerClassReport report,
                                      HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        if (!group.getOwnerId().equals(user.getId())) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        if (report.admit) {
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
        } else {
            group.refuse(user.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * 劝退成员
     *
     * @param groupName   群组名称
     * @param user        要劝退的对象
     * @param httpSession session
     * @return 成功时返回OK
     */
    @RequestMapping(value = "/{groupName}/manage", method = DELETE)
    public ResponseEntity deleteMember(@PathVariable String groupName,
                                       @RequestBody InnerClassUser user,
                                       HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User userFound = userRepository.findById(user.userId);
        Group group = groupRepository.findOneByGroupName(groupName);
        if (userFound == null || group == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        if (!group.getOwnerId().equals(httpSession.getAttribute("userId").toString())) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        if (group.getMemberId().remove(user.userId)) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * GET获取组成员列表
     *
     * @param groupName 获取组的组名
     * @param pageable 请求分页参数
     *                 @see Pageable
     * @param httpSession 请求会话
     * @return 成员列表
     */
    @RequestMapping(value = "/{groupName}/members", method = GET)
    public ResponseEntity retriveMember(@PathVariable String groupName,
                                        Pageable pageable,
                                        HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        if (group.getType().equals(GroupType.CLOSE)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(
                    new PageImpl<>(group.getMemberId()
                            .stream()
                            .map(userRepository::findById)
                            .map(UserHeader::new)
                            .collect(Collectors.toList()),
                            pageable,
                            group.getMemberId().size()),
                    HttpStatus.OK);
        }
    }

    /**
     * POST发送Pull Request
     *
     * @param groupName           目标题单所在的组的组名
     * @param bindingProblemLists 绑定的源题单和目标题单对象
     * @param session             请求会话
     * @return PR是否发送成功
     */
    @RequestMapping(value = "/{groupName}/pullReq", method = POST)
    public ResponseEntity sendPullRequest(@PathVariable String groupName,
                                          @RequestBody BindingProblemLists bindingProblemLists,
                                          HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        Group tempGroup = groupRepository.findOneByGroupName(groupName);

        if (tempGroup == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }

        String groupOwnerId = tempGroup.getOwnerId();
        String userId = session.getAttribute("userId").toString();

        if (groupOwnerId.equals(userId)) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        User groupOwner = userRepository.findById(groupOwnerId);

        Message tempMessage = new Message("Pull request",
                userRepository.findById(userId).getUsername() + "wants to merge into problemLists.",
                MessageType.PROPOSAL, groupName);

        bindingProblemLists.setMessage(tempMessage);

        List<BindingProblemLists> binding = tempGroup.getBindingProblemLists();

        groupRepository.save(tempGroup);

        if (!binding.contains(bindingProblemLists)) {
            binding.add(bindingProblemLists);
        }

        groupOwner.getMessages().add(tempMessage);
        userRepository.save(groupOwner);

        return new ResponseEntity<>(HttpStatus.OK);
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

    class InnerClassUser {
        public String userId;

        public InnerClassUser(String objectId) {
            this.userId = objectId;
        }
    }

    class InnerClassReport {
        boolean admit;

        /**
         * @param admit true for admit/false for refuse
         */
        public InnerClassReport(boolean admit) {
            this.admit = admit;
        }
    }
}
