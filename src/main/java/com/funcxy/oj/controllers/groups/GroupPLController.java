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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * @author ddhee
 * 权限说明：
 * 只有管理员有权创建/修改/删除题单
 * 对于open和free的群组，所有人（包括外部人员）可以查看题单列表，close的群组只有群组内成员可以查看题单列表
 * 群组内成员可以查看题单
 */

@RestController
@RequestMapping("/groups")
public class GroupPLController {
    private final
    GroupRepository groupRepository;
    private final
    UserRepository userRepository;
    private final
    ProblemListRepository problemListRepository;
    private final
    ProblemRepository problemRepository;

    @Autowired
    public GroupPLController(GroupRepository groupRepository, UserRepository userRepository, ProblemListRepository problemListRepository,ProblemRepository problemRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.problemListRepository = problemListRepository;
        this.problemRepository = problemRepository;
    }

    // 创建题单
    @RequestMapping(value = "/{groupName}/problemList", method = RequestMethod.POST)
    public ResponseEntity createProblemList(@PathVariable String groupName,
                                            @RequestBody @Valid ProblemList problemList,
                                            HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        Group group = groupRepository.findOneByGroupName(groupName);
        if (user == null || group == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        if (!group.getOwnerId().equals(user.getId())){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        if (!problemList.isPublic()) {
            problemList.setUserList(null);
        }
        problemList.setCreator(httpSession.getAttribute("userId").toString());
        problemListRepository.save(problemList);
        group.addProblemListOwned(problemList.getId());
        groupRepository.save(group);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 获取题单列表
    @RequestMapping(value = "/{groupName}/problemList",method = RequestMethod.GET)
    public ResponseEntity getProblemList(@PathVariable String groupName,
                                              Pageable pageable,
                                              HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        Group group = groupRepository.findOneByGroupName(groupName);
        if (group == null || user == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        if (group.getType().equals(GroupType.CLOSE) && !user.getGroupIn().contains(user.getId())) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(
                new PageImpl<>(
                        group.getOwnedProblemList()
                                .stream()
                                .map(problemListRepository::findById)
                                .collect(Collectors.toList()),
                        pageable,
                        group.getOwnedProblemList().size()
                ),
                HttpStatus.OK);
    }


}
