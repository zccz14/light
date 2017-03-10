package com.funcxy.oj.controllers.groups;

import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.Group;
import com.funcxy.oj.models.GroupType;
import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.GroupRepository;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.UserUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.stream.Collectors;

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
    //修改题单
    @RequestMapping(value = "/{groupName}/problemList",method = RequestMethod.PUT)
    public ResponseEntity updateProblemList(@PathVariable String groupName,
                                            @RequestBody @Valid ProblemList problemList,
                                            HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
        Group group = groupRepository.findOneByGroupName(groupName);
        if (user == null || group == null){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }
        if (!user.getGroupIn().contains(group.getId())){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        problemListRepository.save(problemList);
        return new ResponseEntity<>(problemList,HttpStatus.OK);
    }
    // 删除题单
    @RequestMapping(value = "/{groupName}/problemList",method = RequestMethod.DELETE)
    public ResponseEntity deleteProblemList(@PathVariable String groupName,
                                           @RequestBody ProblemList problemList,
                                           HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
        Group group = groupRepository.findOneByGroupName(groupName);
        if (user == null ||group == null){
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
    // 获取题单列表
    @RequestMapping(value = "/{groupName}/problemList",method = RequestMethod.GET)
    public ResponseEntity retrieveProblemList(@PathVariable String groupName,
                                              Pageable pageable,
                                             HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null || user == null){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }
        if (group.getType().equals(GroupType.CLOSE)&&!user.getGroupIn().contains(user.getId())){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(
                new PageImpl<ProblemList>(
                        group.getOwnedProblemList()
                        .stream()
                        .map(id->problemListRepository.findById(id))
                        .collect(Collectors.toList()),
                        pageable,
                        group.getOwnedProblemList().size()
                ),
                HttpStatus.OK);
    }
    // TODO:查看单个题单
    // TODO:添加题单的题目
    // TODO:删除题单的题目
}
