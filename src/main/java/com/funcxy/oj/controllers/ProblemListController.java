package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.*;
import com.funcxy.oj.models.Group;
import com.funcxy.oj.models.Problem;
import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.GroupRepository;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.ProblemRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.DataPageable;
import com.funcxy.oj.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import static com.funcxy.oj.utils.UploadFiles.upload;
import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * @author Peter
 * @version 1.0
 */

@RestController
@RequestMapping("/problemLists")
public class ProblemListController {
    private static final Sort sort = new Sort(Sort.Direction.ASC, "title");

    private final ProblemListRepository problemListRepository;

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    private final ProblemRepository problemRepository;

    private DataPageable pageable;

    {
        pageable = new DataPageable();
        pageable.setSort(sort);
    }


    public boolean isGroup(String id){
        return groupRepository.findById(id)!=null;
    }


    @Autowired
    public ProblemListController(ProblemListRepository problemListRepository, UserRepository userRepository, GroupRepository groupRepository, ProblemRepository problemRepository ) {
        this.problemListRepository = problemListRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.problemRepository = problemRepository;
    }

        // 后端题单检索功能，版权所有，请勿删除。
//        if (creator != null && title != null) {
//            return new ResponseEntity(problemListRepository.findByCreatorLikeAndTitleLike(creator, title, pageable), HttpStatus.OK);
//        } else if (creator != null) {
//            return new ResponseEntity(problemListRepository.findByCreatorLike(creator, pageable), HttpStatus.OK);
//        } else if (title != null) {
//            return new ResponseEntity(problemListRepository.findByTitleLike(title, pageable), HttpStatus.OK);
//        } else {
//            return new ResponseEntity(problemListRepository.findAll(pageable), HttpStatus.OK);
//        }

    /**
     * 获取题单列表
     * @param pageNumber 当前页码
     * @param pageSize 页面大小（一页的元素数量）
     * @param session session
     * @return 成功时返回题单列表
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblemLists(@RequestParam int pageNumber,
                                          @RequestParam int pageSize,
                                          HttpSession session) {
        pageable.setPageNumber(pageNumber);
        pageable.setPageSize(pageSize);

        if (!isSignedIn(session)) {
            return new ResponseEntity<>(problemListRepository.findByIsPublic(true, pageable), HttpStatus.OK);
        }

        String userId = session.getAttribute("userId").toString();

        return new ResponseEntity<>(problemListRepository
                .findByIsPublicOrCreatorOrUserListLike(true,
                        userId, userId, pageable), HttpStatus.OK);
    }

    /**
     * 查看单个题单
     * @param id 题单id
     * @param session session
     * @return 返回单个题单
     * 权限说明：
     * 可以查看的题单为：
     * 1.公开的题单
     * 2.所在群组题单
     * 3.授权列表包含该用户的私有题单
     * 此api只能于登录后调用
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblemList(@PathVariable String id,
                                                    HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        ProblemList tempProblemList = problemListRepository.findById(id);

        if (tempProblemList == null) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        String tempObjectId = session.getAttribute("userId").toString();
        User user = userRepository.findById(tempObjectId);

        if (tempProblemList.getCreator().equals(tempObjectId)) {
            return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
        }

        if (tempProblemList.isPublic() ||
                (groupRepository.findById(tempProblemList.getCreator())!=null
                        &&user.getGroupIn().contains(tempProblemList.getCreator()))||
                tempProblemList
                        .getUserList()
                        .contains(tempObjectId)) {
            Date readBeginTime = tempProblemList.getReadBeginTime();
            Date readEndTime = tempProblemList.getReadEndTime();

            Date now = new Date(System.currentTimeMillis());

            if ((readBeginTime == null && readEndTime == null)) {
                return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
            }

            if (readBeginTime != null) {
                if (readBeginTime.before(now) && readEndTime == null) {
                    return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
                } else if (readBeginTime.after(now) && readEndTime == null) {
                    return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
                }
            }

            if (readEndTime != null) {
                if (readEndTime.after(now) && readBeginTime == null) {
                    return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
                } else if (readEndTime.before(now) && readBeginTime == null) {
                    return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
                }
            }

            if (readBeginTime != null && readEndTime != null) {
                if (readBeginTime.before(now) && readEndTime.after(now)) {
                    return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
    }

    /**
     * 创建题单API
     * @param problemList 题单对象
     * @param session session
     * @return 成功时返回题单对象
     * 此API针对个人创建题单，登陆后使用
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createProblemList(@Valid @RequestBody ProblemList problemList,
                                            HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        if (!problemList.isPublic()) {
            problemList.setUserList(null);
        }

        String tempObjectId = session.getAttribute("userId").toString();

        problemList.setCreator(tempObjectId);
        ProblemList tempProblemList = problemListRepository.save(problemList);

        User user = userRepository.findById(tempObjectId);
        user.addProblemListOwned(tempProblemList.getId());
        userRepository.save(user);

        return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
    }

    /**
     * 上传封面
     * @param cover 封面文件
     * @param session session
     * @return 返回文件路径
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity createCoverTest(@RequestBody MultipartFile cover,
                                          HttpSession session) {
        if (isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        if (cover.getOriginalFilename().matches("^\\S*.((jpg$)|(png$)|(bmp$))")) {
            Properties properties = new Properties();
            try {
                properties.load(new BufferedInputStream(new FileInputStream(new File("").getAbsolutePath() + "\\src\\main\\resources\\project.properties")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(upload(cover, properties.getProperty("coverPath")), HttpStatus.OK);
        }

        return new ResponseEntity<>(new UnsupportedMediaType(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * 修改题单API
     * @param problemList 题单对象
     * @param id 题单id
     * @param session session
     * @return 成功时返回题单
     * 此API针对用户和群组
     * 只有当用户为题单创建者或题单对应群组所有者时可修改
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modifyProblemList(@RequestBody @Valid ProblemList problemList,
                                            @PathVariable String id,
                                            HttpSession session) {
        String tempObjectId = session.getAttribute("userId").toString();
        ProblemList tempProblemList = problemListRepository.findById(id);
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        if (tempProblemList==null||userRepository.findById(tempObjectId)==null){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }
        if ((!tempProblemList.getCreator().equals(tempObjectId))
                &&(groupRepository.findById(tempProblemList.getCreator())==null
                ||!groupRepository.findById(tempProblemList.getCreator()).getOwnerId().equals(tempObjectId))){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }
        if (problemList.isPublic()) {
            problemList.setUserList(null);
        }
        problemList.setId(id);
        problemList.setCreator(tempObjectId);
        problemListRepository.save(problemList);
        return new ResponseEntity<>(problemListRepository.save(problemList), HttpStatus.OK);
    }

    /**
     * 删除题单
     * @param id 题单id
     * @param session session
     * @return 成功时返回OK
     * 针对用户和群组
     * 权限与修改相同
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblemList(@PathVariable String id,
                                            HttpSession session) {
        ProblemList tempProblemList = problemListRepository.findById(id);

        String tempObjectId = session.getAttribute("userId").toString();

        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        if (tempProblemList == null || userRepository.findById(tempObjectId)==null){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }

        if ((!tempProblemList.getCreator().equals(tempObjectId))
                &&(groupRepository.findById(tempProblemList.getCreator())==null
                ||!groupRepository.findById(tempProblemList.getCreator()).getOwnerId().equals(tempObjectId))){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }

        problemListRepository.delete(tempProblemList);
        User user = userRepository.findById(tempObjectId);
        user.deleteProblemListOwned(tempProblemList.getId());
        userRepository.save(user);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    /**
     * 添加题目到题单
     * @param problemListId 题单id
     * @param problem 题目对象
     * @param httpSession session
     * @return 成功时返回新problem对象
     * 针对用户和群组，权限与修改相同
     */
    @RequestMapping(value = "/{problemListId}",method = RequestMethod.POST)
    public ResponseEntity addProblem(@PathVariable String problemListId,
                                     @RequestBody Problem problem,
                                     HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        ProblemList problemList = problemListRepository.findById(problemListId);
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        problem = problemRepository.findById(problem.getId());
        if ( problemList == null || user == null || problem==null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        //鉴权
        if ((!problemList.getCreator().equals(user.getId()))
                &&(groupRepository.findById(problemList.getCreator())==null
                ||!groupRepository.findById(problemList.getCreator()).getOwnerId().equals(user.getId()))){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }

        if (problemList.getProblemIds().contains(problem.getId())){
            return new ResponseEntity<>(new FieldsDuplicateError(),HttpStatus.BAD_REQUEST);
        }

        problemList.getProblemIds().add(problem.getId());
        problemListRepository.save(problemList);
        return new ResponseEntity<>(problemList,HttpStatus.OK);
    }

    /**
     * 删除题单中的单个问题
     * @param problemListId 题单Id
     * @param problemId 待删除题目Id
     * @param httpSession session
     * @return 成功时return新题单
     * 针对group和user
     */
    @RequestMapping(value = "/{problemListId}/{problemId}",method = RequestMethod.DELETE)
    public ResponseEntity deleteProblem(@PathVariable String problemListId,
                                        @PathVariable String problemId,
                                        HttpSession httpSession){
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        ProblemList problemList = problemListRepository.findById(problemListId);
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        Problem problem = problemRepository.findById(problemId);
        if ( problemList == null || user == null || problem==null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        //鉴权
        if ((!problemList.getCreator().equals(user.getId()))
                &&(groupRepository.findById(problemList.getCreator())==null
                ||!groupRepository.findById(problemList.getCreator()).getOwnerId().equals(user.getId()))){
            return new ResponseEntity<>(new ForbiddenError(),HttpStatus.FORBIDDEN);
        }

        if (!problemList.getProblemIds().contains(problem.getId())){
            return new ResponseEntity<>(new NotFoundError(),HttpStatus.NOT_FOUND);
        }

        problemList.getProblemIds().remove(problem.getId());
        problemListRepository.save(problemList);
        return new ResponseEntity<>(problemList,HttpStatus.OK);
    }
}