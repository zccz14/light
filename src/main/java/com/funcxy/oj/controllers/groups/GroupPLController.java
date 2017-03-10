package com.funcxy.oj.controllers.groups;

import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.Group;
import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.GroupRepository;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.UserUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author ddhee
 */

@RestController
@RequestMapping("/groups")
public class GroupPLController {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProblemListRepository problemListRepository;
    // 创建题单
    @RequestMapping(value = "/{groupName}/problemList",method = RequestMethod.POST)
    public ResponseEntity createProblemList(@PathVariable String groupName,
                                            @RequestBody @Valid ProblemList problemList,
                                            HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
        Group group = groupRepository.findOneByGroupName(groupName);
        if (user == null || group == null){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }
        if (!problemList.isAccessible()){
            problemList.setUserList(null);
        }
        problemList.setCreator(new ObjectId(httpSession.getAttribute("userId").toString()));
        problemList.setCreatedTime(new Date());
        problemListRepository.save(problemList);
        group.addProblemListOwned(problemList.getId());
        groupRepository.save(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //TODO:修改题单

    // 删除题单
 @RequestMapping(value = "/{groupName}/problemList",method = RequestMethod.DELETE)
    public ResponseEntity deleteProblemList(@PathVariable String groupName,
                                           @RequestBody ProblemList problemList,
                                           HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        ProblemList problemListFound = problemListRepository.findById(problemList.getId());
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
        Group group = groupRepository.findOneByGroupName(groupName);
        if (user == null || problemListFound == null){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }
//        if (!problemListFound.getCreator().equals(user.getId())){
//            return new ResponseEntity<>(new NotFoundError(),HttpStatus.FORBIDDEN);
//        }
        if (!user.getGroupIn().contains(group.getId())){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        group.deleteProblemListOwned(problemList.getId());
        problemListRepository.delete(problemList);
        groupRepository.save(group);
        return new ResponseEntity(HttpStatus.OK);
    }
    // TODO:获取题单列表
    // TODO:添加题单的题目
    // TODO:删除题单的题目
}
