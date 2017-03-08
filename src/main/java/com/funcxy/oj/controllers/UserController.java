package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.*;
import com.funcxy.oj.models.*;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.ProblemRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.DataPageable;
import com.funcxy.oj.utils.InvalidException;
import com.funcxy.oj.utils.UserUtil;
import com.funcxy.oj.utils.Validation;
import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import org.bson.types.ObjectId;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private UserRepository userRepository;
    @Autowired
    private ProblemListRepository problemListRepository;
    @Autowired
    private ProblemRepository problemRepository;
    private DataPageable pageable;
    {
        pageable = new DataPageable();
        pageable.setSort(new Sort(Sort.Direction.ASC,"title"));
    }

    @RequestMapping(value = "/sign-in", method = POST)//登录
    public ResponseEntity<Object> signIn(@RequestBody Passport passport, HttpSession httpSession) throws InvalidException{
        if(passport.username==null){
            return new ResponseEntity<>(new InvalidException("username or email must be set"),HttpStatus.BAD_REQUEST);
        }else{
            System.out.println(passport.username+"login");
            RegularExpression regExpEmail = new RegularExpression("^\\S+@[a-zA-Z0-9]+\\.[a-zA-Z]+");
            RegularExpression regExpUsername = new RegularExpression("^[a-zA-Z0-9_]+");
            if (regExpEmail.matches(passport.username)){
                User userFound = userRepository.findOneByEmail(passport.username);
                if (userFound == null)return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
                System.out.println(userFound);
                if (userFound.passwordVerify(passport.password)){
                    httpSession.setAttribute("userId",userFound.getId().toString());
                    return new ResponseEntity<>(userFound, HttpStatus.OK);
                }
            }else  if(regExpUsername.matches(passport.username)){
                User userFound = userRepository.findOneByUsername(passport.username);
                if(userFound == null) return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
                System.out.println("found by username"+userFound);
                if (userFound.passwordVerify(passport.password)){
                    httpSession.setAttribute("userId",userFound.getId().toString());
                    return new ResponseEntity<>(userFound,HttpStatus.OK);
                }
                System.out.println("password wrong"+passport.password);
                return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
            }else {
//                throw new InvalidException("input illegal");
                return new ResponseEntity<>(new FieldsRequiredError(), HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/sign-up", method = POST)//注册
    public ResponseEntity<Object> signUp(@RequestBody @Valid Passport passport, HttpSession httpSession) throws InvalidException{
        System.out.println(passport.username+passport.email+passport.password);
        if(Validation.isValid(passport)){
            System.out.println(passport.username+" sign up");
            User userFoundByUsername = userRepository.findOneByUsername(passport.email);
            if (userFoundByUsername!=null){
                return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
            }
            User userFoundByEmail = userRepository.findOneByEmail(passport.email);
            if (userFoundByEmail!=null) {
                //查看邮箱是否已验证
                System.out.println("foundbyemail"+userFoundByEmail.getEmail());
                if(!userFoundByEmail.hasVerifiedEmail()){
                    userFoundByEmail.setUsername(passport.username);
                    userFoundByEmail.setPassword(passport.password);
                    userFoundByEmail.notVerified();
                    userRepository.save(userFoundByEmail);
                    //发邮件
                    UserUtil.sendEmail(userFoundByEmail.getEmail(),userFoundByEmail.getUsername()+"/"+userFoundByEmail.getIdentifyString());
                    return new ResponseEntity<Object>(userFoundByEmail,HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
                }
            }
            User user = new User();
            user.setUsername(passport.username);
            user.setEmail(passport.email);
            user.setPassword(passport.password);
            user.notVerified();
            //发邮件
            UserUtil.sendEmail(user.getEmail(),user.getUsername()+"/"+user.getIdentifyString());
            return new ResponseEntity<>(userRepository.insert(user), HttpStatus.CREATED);
        }else {
            System.out.println("invalid passport");
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/{username}/profile", method = GET)//获取详细资料
    public ResponseEntity<Object> profile(HttpSession httpSession, @PathVariable String username) throws InvalidException{
          if (userRepository.findOneByUsername(username)==null)
              return new ResponseEntity<>(new BadRequestError(), HttpStatus.NOT_FOUND);
          else return new ResponseEntity<>(userRepository.findOneByUsername(username).getProfile(),  HttpStatus.FOUND);
    }

    @RequestMapping(value = "/{username}/profile",method = PUT)//修改用户资料
    public ResponseEntity putProfile(@RequestBody @Valid Profile profile,@PathVariable String username, HttpSession httpSession){
        if(UserUtil.isSignedIn(httpSession)){
            User userFound = userRepository.findById((ObjectId) httpSession.getAttribute("userId"));
            userFound.setProfile(profile);
            userRepository.save(userFound);
            return new ResponseEntity<>(profile,HttpStatus.OK);
        }
        return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/find/username",method = GET)//精确查找用户名
    public ResponseEntity hasUsername(@RequestParam String username){
        User userFound = userRepository.findOneByUsername(username);
        if (userFound == null)return new ResponseEntity<>("not found",HttpStatus.OK);
        return new ResponseEntity<>("find",HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/find/email",method = GET)//精确查找邮箱
    public ResponseEntity hasEmail(@RequestParam String email){
        User userFound = userRepository.findOneByEmail(email);
        if(userFound == null)return new ResponseEntity<>("not found",HttpStatus.OK);
        return new ResponseEntity<>("find",HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/{username}/{verify}",method = GET)//验证邮箱
    public ResponseEntity verifyEmail(@PathVariable String username,@PathVariable String verify){
        User user = userRepository.findOneByUsername(username);
        if(user.hasVerifiedEmail()){
            return new ResponseEntity<>(new FieldsInvalidError(),HttpStatus.NO_CONTENT);//已经验证过
        }else {
            if (user.toVerifyEmail(verify)){
                user.verifyingEmail();
                userRepository.save(user);
                return new ResponseEntity<>("success",HttpStatus.ACCEPTED);
            }else {
                return new ResponseEntity<>(new BadRequestError(),HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "/search",method = GET)//模糊查找多个用户
    public ResponseEntity searchUser(@RequestParam String email, String username, String nickname, String bio, String location, org.springframework.data.domain.Pageable pageable){
        List<User> users = new ArrayList<>(0);
        if (email != null){
            users.addAll(userRepository.findByEmail(email));
        }
        if (username != null){
            if (users.size() != 0){//非空时取交集
                List<User> usersFoundByUsername = userRepository.findByUsernameLike(username);
                users.retainAll(usersFoundByUsername);
            }else{//空时直接添加
                users.addAll(userRepository.findByUsernameLike(username));
            }
        }
        if (nickname != null){
            if (users.size()!=0){
                List<User> usersFoundByNickname = userRepository.findByNicknameLike(nickname);
                users.retainAll(usersFoundByNickname);
            }else{
                users.addAll(userRepository.findByNicknameLike(nickname));
            }
        }
        if (location != null){
            if (users.size()!=0){
                List<User> usersFoundByLocation = userRepository.findByLocation(location);
                users.retainAll(usersFoundByLocation);
            }else {
                users.addAll(userRepository.findByLocation(location));
            }
        }
        if (bio != null){
            if (users.size() != 0){
                List<User> usersFoundByBio = userRepository.findByBioLike(bio);
                users.retainAll(usersFoundByBio);
            }else {
                users.addAll(userRepository.findByBioLike(bio));
            }
        }
        return new ResponseEntity<>(new PageImpl<User>(users,pageable,users.size())
                ,HttpStatus.OK);
    }

}
