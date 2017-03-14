package com.funcxy.oj.controllers;

import com.funcxy.oj.contents.BindingProblemLists;
import com.funcxy.oj.contents.Passport;
import com.funcxy.oj.contents.SignInPassport;
import com.funcxy.oj.errors.*;
import com.funcxy.oj.models.*;
import com.funcxy.oj.repositories.GroupRepository;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.ProblemRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.UserUtil;
import com.funcxy.oj.utils.Validation;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * User Controller
 *
 * @author zccz14 aak1247 Peter
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final ProblemListRepository problemListRepository;
    private final ProblemRepository problemRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public UserController(UserRepository userRepository, ProblemListRepository problemListRepository, ProblemRepository problemRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.problemListRepository = problemListRepository;
        this.problemRepository = problemRepository;
        this.groupRepository = groupRepository;
    }

    @RequestMapping(value = "/sign-in", method = POST)//登录
    public ResponseEntity signIn(@RequestBody SignInPassport passport, HttpSession httpSession) {
        if (passport.username == null) {
            return new ResponseEntity<>(new FieldsRequiredError(), HttpStatus.BAD_REQUEST);
        } else {
            System.out.println(passport.username + "login");
            RegularExpression regExpEmail = new RegularExpression("^\\S+@[a-zA-Z0-9]+\\.[a-zA-Z]+");
            RegularExpression regExpUsername = new RegularExpression("^[a-zA-Z0-9_]+");
            if (regExpEmail.matches(passport.username)) {
                User userFound = userRepository.findOneByEmail(passport.username);
                if (userFound == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
                System.out.println(userFound);
                if (userFound.passwordVerify(passport.password)) {
                    httpSession.setAttribute("userId", userFound.getId());
                    return new ResponseEntity<>(userFound, HttpStatus.OK);
                }
            } else if (regExpUsername.matches(passport.username)) {
                User userFound = userRepository.findOneByUsername(passport.username);
                if (userFound == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
                System.out.println("found by username" + userFound);
                if (userFound.passwordVerify(passport.password)) {
                    httpSession.setAttribute("userId", userFound.getId());
                    return new ResponseEntity<>(userFound, HttpStatus.OK);
                }
                System.out.println("password wrong" + passport.password);
                return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(new FieldsRequiredError(), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/sign-up", method = POST)//注册
    public ResponseEntity signUp(@RequestBody @Valid Passport passport, HttpSession httpSession) {
        System.out.println(passport.username + passport.email + passport.password);
        if (Validation.isValid(passport)) {
            System.out.println(passport.username + " sign up");
            User userFoundByUsername = userRepository.findOneByUsername(passport.email);
            if (userFoundByUsername != null) {
                return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
            }
            User userFoundByEmail = userRepository.findOneByEmail(passport.email);
            if (userFoundByEmail != null) {
                //查看邮箱是否已验证
                System.out.println("foundbyemail" + userFoundByEmail.getEmail());
                if (!userFoundByEmail.hasVerifiedEmail()) {
                    userFoundByEmail.setUsername(passport.username);
                    userFoundByEmail.setPassword(passport.password);
                    userFoundByEmail.notVerified();
                    userRepository.save(userFoundByEmail);
                    //发邮件
                    UserUtil.sendVerifyEmail(userFoundByEmail.getEmail(), userFoundByEmail.getUsername() + "/" + userFoundByEmail.getIdentifyString());
                    return new ResponseEntity<>(userFoundByEmail, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
                }
            }
            User user = new User();
            user.setUsername(passport.username);
            user.setEmail(passport.email);
            user.setPassword(passport.password);
            user.notVerified();
            //发邮件
            UserUtil.sendVerifyEmail(user.getEmail(), user.getUsername() + "/" + user.getIdentifyString());
            return new ResponseEntity<>(userRepository.insert(user), HttpStatus.CREATED);
        } else {
            System.out.println("invalid passport");
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/{username}/profile", method = GET)//获取详细资料
    public ResponseEntity profile(HttpSession httpSession, @PathVariable String username) {
        if (userRepository.findOneByUsername(username) == null) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userRepository.findOneByUsername(username).getProfile(), HttpStatus.FOUND);
        }
    }

    @RequestMapping(value = "/{username}/profile", method = PUT)//修改用户资料
    public ResponseEntity putProfile(@RequestBody @Valid Profile profile, @PathVariable String username, HttpSession httpSession) {
        if (isSignedIn(httpSession)) {
            User userFound = userRepository.findById(httpSession.getAttribute("userId").toString());
            userFound.setProfile(profile);
            userRepository.save(userFound);
            return new ResponseEntity<>(profile, HttpStatus.OK);
        }
        return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
    }

    /**
     * Head方法，用于查询用户名是否重复
     *
     * @param username 用户名
     * @return 不冲突时返回OK（200）
     */
    @RequestMapping(value = "/find/username", method = HEAD)//精确查找用户名
    public ResponseEntity hasUsername(@RequestParam String username) {
        User userFound = userRepository.findOneByUsername(username);
        if (userFound == null) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * Head方法，用于查询邮箱是否重复
     *
     * @param email 邮箱
     * @return 不冲突时返回OK（200）
     */
    @RequestMapping(value = "/find/email", method = HEAD)//精确查找邮箱
    public ResponseEntity hasEmail(@RequestParam String email) {
        User userFound = userRepository.findOneByEmail(email);
        if (userFound == null) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/{username}/{verify}", method = GET)//验证邮箱
    public ResponseEntity verifyEmail(@PathVariable String username, @PathVariable String verify) {
        User user = userRepository.findOneByUsername(username);
        if (user.hasVerifiedEmail()) {
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.NO_CONTENT);//已经验证过
        } else {
            if (user.toVerifyEmail(verify)) {
                user.verifyingEmail();
                userRepository.save(user);
                return new ResponseEntity<>("success", HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * searchUser
     *
     * @param email    邮箱，支持正则
     * @param username 用户名，支持正则
     * @param nickname 昵称，支持正则
     * @param bio      个人简介，支持正则
     * @param location 所在地，支持正则
     *                 正则表达式中元字符不能单独出现,需在前面加/符号（Java语法的正则表达式）
     * @param pageable 由page size 和 sort三个字段构成，如：
     *                 page=0
     *                 size=10
     *                 sort=username.asc
     */
    @RequestMapping(value = "/search", method = GET)//模糊查找多个用户
    public ResponseEntity searchUser(@RequestParam(defaultValue = "/*") String email,
                                     @RequestParam(defaultValue = "/*") String username,
                                     @RequestParam(defaultValue = "/*") String nickname,
                                     @RequestParam(defaultValue = "/*") String bio,
                                     @RequestParam(defaultValue = "/*") String location,
                                     org.springframework.data.domain.Pageable pageable) {
        Page<User> users = userRepository.roughFind(username, email, nickname, bio, location, pageable);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/{username}/password", method = RequestMethod.POST)
    public ResponseEntity findPassword(@RequestBody Passport passport, @PathVariable String username) {//找回密码
        User user = userRepository.findOneByUsername(passport.username);
        if (user.getEmail().equals(passport.email)) {
            user.findPassword();
            userRepository.save(user);
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{username}/profile/password", method = PUT)
    public ResponseEntity updatePassword(@RequestBody String password, @PathVariable String username, HttpSession httpSession) {//修改密码
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        if (user == null) return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        user.setPassword(password);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * fork 题单
     *
     * @param username    用户名
     * @param problemList 要fork的题单
     * @param httpSession session
     * @return 成功时返回OK
     * 题单无版权
     */
    @RequestMapping(value = "/{username}/fork", method = POST)//fork problemList
    public ResponseEntity forkProblem(@PathVariable String username,
                                      @RequestBody ProblemList problemList,
                                      HttpSession httpSession) {
        if (!isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        if (user.getProblemListForked().contains(problemList.getId())) {
            return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
        }
        if (!problemList.isCanBeCopied()) {
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }
        ProblemList problemListForked;
        problemListForked = problemList;
        problemListForked.setId(ObjectId.get().toString());
        problemListForked.setCreator(user.getId());
        user.addProblemListForked(problemListForked.getId());
        userRepository.save(user);
        problemListRepository.insert(problemListForked);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //TODO:判题API
//    @RequestMapping(value = "/{username}/judge/{submissionId}",method = PUT)
//    public ResponseEntity judge(@PathVariable String username,
//                                @RequestBody )

    /**
     * POST处理Group的邀请信息
     *
     * @param username 当前用户
     * @param message  邀请信息
     * @param accept   是否同意加入Group
     * @param session  请求回话
     * @return 是否加入成功
     */
    @RequestMapping(value = "/{username}/myInvited", method = POST)
    public ResponseEntity handleInvitedMessage(@PathVariable String username,
                                               @RequestBody Message message,
                                               @RequestParam boolean accept,
                                               HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        message.setHasRead(true);

        String tempUserId = session.getAttribute("userId").toString();

        User tempUser = userRepository.findById(tempUserId);

        if (message.getType() != MessageType.INVITATION) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        String groupId = message.getAdditionalInformation();

        Group tempGroup = groupRepository.findById(groupId);

        if (tempGroup == null || tempUser == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }

        if (accept) {
            tempUser.getGroupIn().add(groupId);
            tempGroup.getMemberId().add(tempUserId);
        }

        tempUser.getInvitation().remove(groupId);
        tempGroup.getInvitedMemberId().remove(tempUserId);

        userRepository.save(tempUser);
        groupRepository.save(tempGroup);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST处理PR
     *
     * @param username 当前用户
     * @param index    Message的index
     * @param accept   是否同意PR
     * @param session  请求会话
     * @return 是否PR成功
     */
    @RequestMapping(value = "/{username}/handlePR/{index}", method = POST)
    public ResponseEntity handlePullRequest(@PathVariable String username,
                                            @PathVariable int index,
                                            @RequestParam boolean accept,
                                            HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        String userId = session.getAttribute("userId").toString();
        User groupOwner = userRepository.findById(userId);

        if (groupOwner == null ||
                !groupOwner.getUsername().equals(username)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        Message tempMessage = groupOwner.getMessages().get(index);
        String groupName = tempMessage.getAdditionalInformation();
        Group tempGroup = groupRepository.findOneByGroupName(groupName);
        List<BindingProblemLists> bindings = tempGroup.getBindingProblemLists();
        BindingProblemLists bindingProblemList = bindings.get(bindings.indexOf(tempMessage));

        if (accept) {
            ProblemList source = problemListRepository.findById(bindingProblemList.getSourceProblemListId());
            ProblemList target = problemListRepository.findById(bindingProblemList.getTargetProblemListId());

            List<String> sourceProblemIds = source.getProblemIds();
            List<String> targetProblemIds = target.getProblemIds();

            targetProblemIds.removeAll(sourceProblemIds);
            targetProblemIds.addAll(sourceProblemIds);
        }

        bindings.remove(bindingProblemList);
        groupRepository.save(tempGroup);

        tempMessage.setHasRead(true);
        userRepository.save(groupOwner);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST在用户之间发送私信
     *
     * @param username 当前用户
     * @param message  私信内容（Type为PERSONAL，additionalInformation内为目标用户ID）
     * @param session  请求回话
     * @return 发生成功与否
     */
    @RequestMapping(value = "/{username}/sendPrivateLetter", method = POST)
    public ResponseEntity sendPrivateLetter(@PathVariable String username,
                                            @RequestBody Message message,
                                            HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        User tempUser = userRepository.findById(message.getAdditionalInformation());
        tempUser.getMessages().add(message);
        userRepository.save(tempUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET阅读私信
     *
     * @param username 当前用户
     * @param index    私信条数
     * @param session  请求回话
     * @return 阅读完成
     */
    @RequestMapping(value = "/{username}/readPrivateLetter/{index}", method = GET)
    public ResponseEntity readPrivateLetter(@PathVariable String username,
                                            @PathVariable int index,
                                            HttpSession session) {


        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        String tempUserId = session.getAttribute("userId").toString();
        User tempUser = userRepository.findById(tempUserId);

        if (tempUser == null ||
                !userRepository.findById(tempUserId).getUsername().equals(username)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        tempUser.getMessages().get(index).setHasRead(true);

        userRepository.save(tempUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    //TODO:删除私信


}
