package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.*;
import com.funcxy.oj.models.JudgeProblem;
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
import java.util.List;
import java.util.Properties;

import static com.funcxy.oj.utils.UploadFiles.upload;
import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * Created by wtupc96 on 2017/3/4.
 *
 * @author Peter
 * @version 1.0
 */

@RestController
@RequestMapping("/problemLists")
public class ProblemListController {
    /**
     * 默认按照标题的升序进行排序
     */
    private static final Sort sort = new Sort(Sort.Direction.ASC, "title");

    /**
     * 进行ProblemLists的数据库操作
     */
    private final ProblemListRepository problemListRepository;


    /**
     * 进行Users的数据库操作
     */
    private final UserRepository userRepository;

    /**
     * 进行Groups的数据库操作
     */
    private final GroupRepository groupRepository;

    /**
     * 进行Problems的数据库操作
     */
    private final ProblemRepository problemRepository;

    /**
     * 分页
     */
    private DataPageable pageable;

    /*
     * 分页对象初始化
     */ {
        pageable = new DataPageable();
        pageable.setSort(sort);
    }

    @Autowired
    public ProblemListController(ProblemListRepository problemListRepository, UserRepository userRepository, GroupRepository groupRepository, ProblemRepository problemRepository) {
        this.problemListRepository = problemListRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.problemRepository = problemRepository;
    }

    /*后端题单检索功能，版权所有，请勿删除。
       if (creator != null && title != null) {
           return new ResponseEntity(problemListRepository.findByCreatorLikeAndTitleLike(creator, title, pageable), HttpStatus.OK);
       } else if (creator != null) {
           return new ResponseEntity(problemListRepository.findByCreatorLike(creator, pageable), HttpStatus.OK);
       } else if (title != null) {
           return new ResponseEntity(problemListRepository.findByTitleLike(title, pageable), HttpStatus.OK);
       } else {
           return new ResponseEntity(problemListRepository.findAll(pageable), HttpStatus.OK);
       }
    */

    /**
     * GET题单集合
     *
     * @param pageNumber 页码
     * @param pageSize   页大小
     *                   @see DataPageable
     * @param session    请求的会话
     * @return 是否获取成功
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblemLists(@RequestParam int pageNumber,
                                          @RequestParam int pageSize,
                                          HttpSession session) {
        // 初始化分页参数
        pageable.setPageNumber(pageNumber);
        pageable.setPageSize(pageSize);

        // 鉴权
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(problemListRepository.findByIsPublic(true, pageable), HttpStatus.OK);
            // 未登录只能看到公开题单
        }

        String userId = session.getAttribute("userId").toString();

        // 登录可以看到公开题单，用户创建题单以及用户所在题单
        return new ResponseEntity<>(problemListRepository
                .findByIsPublicOrCreatorOrUserListLike(true,
                        userId, userId, pageable), HttpStatus.OK);
    }

    /**
     * GET查看单个题单
     *
     * @param id      需要查看的题单的ID
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
        // 鉴权
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        ProblemList tempProblemList = problemListRepository.findById(id);

        // 不存在这个题单
        if (tempProblemList == null) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        String tempObjectId = session.getAttribute("userId").toString();
        User user = userRepository.findById(tempObjectId);

        // 是自己创建的题单
        if (tempProblemList.getCreator().equals(tempObjectId)) {
            return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
        }

        if (tempProblemList.isPublic() ||
                (groupRepository.findById(tempProblemList.getCreator()) != null
                        && user.getGroupIn().contains(tempProblemList.getCreator())) ||
                tempProblemList
                        .getUserList()
                        .contains(tempObjectId)) {
            // 获取这个题单的开始阅读时间以及结束阅读时间
            Date readBeginTime = tempProblemList.getReadBeginTime();
            Date readEndTime = tempProblemList.getReadEndTime();

            // 获得现在的时间
            Date now = new Date(System.currentTimeMillis());

            // 如果没有指定开始阅读时间以及结束阅读时间，则认为该题单永久有效
            if ((readBeginTime == null && readEndTime == null)) {
                return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
            }

            // 指定了开始阅读时间
            if (readBeginTime != null && readEndTime == null) {
                // 当前时间在阅读期限之内
                if (readBeginTime.before(now) || readBeginTime.equals(now)) {
                    return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
                } else if (readBeginTime.after(now)) {
                    return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
                }
            }

            // 指定了结束阅读时间
            if (readEndTime != null && readBeginTime == null) {
                // 当前时间在阅读期限之内
                if (readEndTime.after(now) || readEndTime.equals(now)) {
                    return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
                } else if (readEndTime.before(now)) {
                    return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
                }
            }

            // 指定了开始阅读时间以及结束阅读时间并且当前时间在阅读期限之内
            if (readBeginTime != null && readEndTime != null &&
                    (readBeginTime.before(now) && readEndTime.after(now))) {
                return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
            }
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
    }

    /**
     * 创建题单API
     *
     * @param problemList 题单对象
     *                    @see ProblemList
     * @param session     session
     * @return 成功时返回题单对象
     * 此API针对个人创建题单，登陆后使用
     * <p>
     * 前端传输题单时，对于未指定的judger应使用null占位
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createProblemList(@Valid @RequestBody ProblemList problemList,
                                            HttpSession session) {
        // 鉴权
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        // 题单中四个日期的指定是否合法
        if (isNotValidDate(problemList)) {
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }

        // 题单是否公开
        if (!problemList.isPublic()) {
            problemList.setUserList(null);
        }

        String tempObjectId = session.getAttribute("userId").toString();

        // 题单创建者为当前登录的用户
        problemList.setCreator(tempObjectId);
        ProblemList tempProblemList = problemListRepository.save(problemList);

        List<String> problems = problemList.getProblemIds();

        for (int i = 0; i < problems.size(); ++i) {
            if (i >= problemList.getJudgerList().size()) {
                problemList.getJudgerList().add(tempObjectId);
            } else if (problemList.getJudgerList().get(i) == null
                    || problemList.getJudgerList().get(i) == "") {
                problemList.getJudgerList().set(i, tempObjectId);
            }
        }

        // 更新用户创建题单属性
        User user = userRepository.findById(tempObjectId);
        user.addProblemListOwned(tempProblemList.getId());
        userRepository.save(user);

        return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
    }

    /**
     * POST上传题单封面
     *
     * @param cover   封面图片
     *                @see MultipartFile
     * @param session 请求会话
     * @return 是否上传成功
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity createCoverTest(@RequestBody MultipartFile cover,
                                          HttpSession session) {
        // 鉴权
        if (isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        // 判断文件后缀是否合法
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
     *
     * @param problemList 题单对象
     *                    @see ProblemList
     * @param id          题单id
     * @param session     session
     * @return 成功时返回题单
     * 此API针对用户和群组
     * 只有当用户为题单创建者或题单对应群组所有者时可修改
     * 必须保证题目列表和裁判列表一一对应（不等长时会报错）
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

        if (tempProblemList == null || userRepository.findById(tempObjectId) == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }

        // 题单中的四个日期是否合法
        if (isNotValidDate(problemList)) {
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }
        //不能保证一一对应时我是拒绝的
        if (problemList.getJudgerList().size() != problemList.getProblemIds().size()) {
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }

        for (String str : problemList.getJudgerList()) {
            if (str == null) problemList.getJudgerList().set(problemList.getJudgerList().indexOf(str), tempObjectId);
        }

        if ((!tempProblemList.getCreator().equals(tempObjectId))
                && (groupRepository.findById(tempProblemList.getCreator()) == null
                || !groupRepository.findById(tempProblemList.getCreator()).getOwnerId().equals(tempObjectId))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        // 题单是否为公开题单
        if (problemList.isPublic()) {
            problemList.setUserList(null);
        }

        problemList.setId(id);
        problemList.setCreator(tempObjectId);

        return new ResponseEntity<>(problemListRepository.save(problemList), HttpStatus.OK);
    }

    /**
     * 删除题单
     *
     * @param id      目标题单id
     * @param session session
     * @return 是否删除成功，成功时返回OK
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

        if (tempProblemList == null || userRepository.findById(tempObjectId) == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }

        if ((!tempProblemList.getCreator().equals(tempObjectId))
                && (groupRepository.findById(tempProblemList.getCreator()) == null
                || !groupRepository.findById(tempProblemList.getCreator()).getOwnerId().equals(tempObjectId))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        problemListRepository.delete(tempProblemList);

        // 更新用户字段
        User user = userRepository.findById(tempObjectId);
        user.deleteProblemListOwned(tempProblemList.getId());
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 添加题目到题单
     *
     * @param problemListId 题单id
     * @param judgeProblem  包含题目Id和judgerId 其中judgerId可留空
     * @param httpSession   session
     * @return 成功时返回新problem对象
     * 针对用户和群组，权限与修改相同
     */
    @RequestMapping(value = "/{problemListId}", method = RequestMethod.POST)
    public ResponseEntity addProblem(@PathVariable String problemListId,
                                     @RequestBody JudgeProblem judgeProblem,
                                     HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        ProblemList problemList = problemListRepository.findById(problemListId);
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        Problem problem = problemRepository.findById(judgeProblem.getProblemId());
        User judger = null;
        if (judgeProblem.getJudgeId() != null) judger = userRepository.findById(judgeProblem.getJudgeId());
        if (problemList == null || user == null || problem == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        //鉴权
        if ((!problemList.getCreator().equals(user.getId()))
                && (groupRepository.findById(problemList.getCreator()) == null
                || !groupRepository.findById(problemList.getCreator()).getOwnerId().equals(user.getId()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        if (problemList.getProblemIds().contains(problem.getId())) {
            return new ResponseEntity<>(new FieldsDuplicateError(), HttpStatus.BAD_REQUEST);
        }

        problemList.getProblemIds().add(problem.getId());
        //添加对应裁判
        if (judger == null) {
            problemList.getJudgerList().add(user.getId());
        } else {
            problemList.getJudgerList().add(judger.getId());
        }
        problemListRepository.save(problemList);
        return new ResponseEntity<>(problemList, HttpStatus.OK);
    }

    /**
     * 删除题单中的单个问题
     *
     * @param problemListId 题单Id
     * @param problemId     待删除题目Id
     * @param httpSession   session
     * @return 成功时return新题单
     * 针对group和user
     */
    @RequestMapping(value = "/{problemListId}/{problemId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblem(@PathVariable String problemListId,
                                        @PathVariable String problemId,
                                        HttpSession httpSession) {
        if (!UserUtil.isSignedIn(httpSession)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        ProblemList problemList = problemListRepository.findById(problemListId);
        User user = userRepository.findById(httpSession.getAttribute("userId").toString());
        Problem problem = problemRepository.findById(problemId);
        if (problemList == null || user == null || problem == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        //鉴权
        if ((!problemList.getCreator().equals(user.getId()))
                && (groupRepository.findById(problemList.getCreator()) == null
                || !groupRepository.findById(problemList.getCreator()).getOwnerId().equals(user.getId()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        if (!problemList.getProblemIds().contains(problem.getId())) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        //删除裁判
        problemList.getJudgerList().remove(problemList.getProblemIds().indexOf(problem.getId()));
        problemList.getProblemIds().remove(problem.getId());
        problemListRepository.save(problemList);
        return new ResponseEntity<>(problemList, HttpStatus.OK);
    }

    /**
     * 判断题单中的四个时间字段是否合法
     *
     * @param problemList 需要进行合法判断的题单
     *                    @see ProblemList
     * @return true：日期合法，反之
     */
    private boolean isNotValidDate(ProblemList problemList) {
        Date readBeginTime = problemList.getReadBeginTime();
        Date readEndTime = problemList.getReadEndTime();
        Date answerBeginTime = problemList.getAnswerBeginTime();
        Date answerEndTime = problemList.getAnswerEndTime();

        /*
        * 1. readBeginTime < readEndTime
        * 2. answerBeginTime < answerEndTime
        * 3. readBeginTime < answerBeginTime
        * 4. answerEndTime < readEndTime
        * */
        if (readBeginTime == null || readEndTime == null) {
            if (answerBeginTime != null && answerEndTime != null) {
                return answerBeginTime.before(answerEndTime);
            }
            return true;
        }

        if (readBeginTime.before(readEndTime)) {
            if (answerBeginTime != null && answerEndTime != null) {
                return answerBeginTime.before(answerEndTime);
            }
            return true;
        }

        return false;
    }
}