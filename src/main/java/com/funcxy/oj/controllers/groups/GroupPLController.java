package com.funcxy.oj.controllers.groups;

import com.funcxy.oj.errors.FieldsDuplicateError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.*;
import com.funcxy.oj.repositories.GroupRepository;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.ProblemRepository;
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
 * 权限说明：
 * 群组成员都可以创建题单
 * 只有管理员和题单创建者有权修改/删除题单
 * 创建者退出群组后失去权限
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
    @Autowired
    ProblemRepository problemRepository;
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
//        if (problemList.getCreator().equals(user.getId())||group.getOwnerId().equals(user.getId())){
            problemListRepository.save(problemList);
            return new ResponseEntity<>(problemList,HttpStatus.OK);
//        }
//        return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
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
        if (!user.getGroupIn().contains(group.getId())){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
//        if (problemList.getCreator().equals(user.getId())||group.getOwnerId().equals(user.getId())){
            group.deleteProblemListOwned(problemList.getId());
            problemListRepository.delete(problemList);
            groupRepository.save(group);
            return new ResponseEntity(HttpStatus.OK);
//        }
//        return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
    }
    // 获取题单列表
    @RequestMapping(value = "/{groupName}/problemList",method = RequestMethod.GET)
    public ResponseEntity getProblemList(@PathVariable String groupName,
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
    // 查看单个题单:路由到题单下对应router
//    @RequestMapping(value = "/{groupName}/problemList/{problemListTitle}",method = RequestMethod.GET)
//    public ResponseEntity retrieveProblemList(@PathVariable String groupName,
//                                              @PathVariable String problemListTitle,
//                                              HttpSession httpSession){
//        if (!UserUtil.isSignedIn(httpSession)){
//            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
//        }
//        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
//        Group group = groupRepository.findOneByGroupName(groupName);
//        ProblemList problemList = problemListRepository.findOneByTitle(problemListTitle);
//        if (user == null || group == null || problemList == null){
//            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
//        }
//        if (user.getGroupIn().contains(group.getId())){
//            return new ResponseEntity<>(problemList,HttpStatus.OK);
//        }
//        return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
//    }
    // 添加题单的题目
    @RequestMapping(value = "/{groupName}/problemList/{problemListTitle}",method = RequestMethod.POST)
    public ResponseEntity addProblem(@PathVariable String groupName,
                                     @PathVariable String problemListTitle,
                                     @RequestBody Problem problem,
                                     HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Group group = groupRepository.findOneByGroupName(groupName);
        ProblemList problemList = problemListRepository.findOneByTitle(problemListTitle);
        User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
        problem = problemRepository.findById(problem.getId());
        if (group == null || problemList == null || user == null || problem==null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        if (!user.getGroupIn().contains(group.getId())) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
//        if (problemList.getCreator().equals(user.getId()) || group.getOwnerId().equals(user.getId())) {
            if (problemList.getProblemIds().contains(problem.getId())){
                return new ResponseEntity<>(new FieldsDuplicateError(),HttpStatus.BAD_REQUEST);
            }
            problemList.getProblemIds().add(problem.getId());
            problemListRepository.save(problemList);
            return new ResponseEntity<>(problemList,HttpStatus.OK);
//        }
//        return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
    }
    // 删除题单的题目
    @RequestMapping(value = "/{groupName}/problemList/{problemListTitle}",method = RequestMethod.DELETE)
    public ResponseEntity deleteProblem(@PathVariable String groupName,
                                        @PathVariable String problemListTitle,
                                        @RequestBody Problem problem,
                                        HttpSession httpSession){
            if (!UserUtil.isSignedIn(httpSession)) {
                return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
            }
            Group group = groupRepository.findOneByGroupName(groupName);
            ProblemList problemList = problemListRepository.findOneByTitle(problemListTitle);
            User user = userRepository.findById(new ObjectId(httpSession.getAttribute("userId").toString()));
            problem = problemRepository.findById(problem.getId());
            if (group == null || problemList == null || user == null || problem==null) {
                return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
            }
            if (!user.getGroupIn().contains(group.getId())) {
                return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
            }
//            if (problemList.getCreator().equals(user.getId()) || group.getOwnerId().equals(user.getId())){
                if (!problemList.getProblemIds().contains(problem.getId())){
                    return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
                }
                problemList.getProblemIds().remove(problem.getId());
                problemListRepository.save(problemList);
                return new ResponseEntity<>(problemList,HttpStatus.OK);
//            }
//            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
    }
}
