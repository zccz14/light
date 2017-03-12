package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.BadRequestError;
import com.funcxy.oj.errors.FieldsInvalidError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.UnsupportedMediaType;
import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.DataPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    private final MongoTemplate mongoTemplate;

    /**
     * 进行Users的数据库操作
     */
    private final UserRepository userRepository;

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
    public ProblemListController(ProblemListRepository problemListRepository, MongoTemplate mongoTemplate, UserRepository userRepository) {
        this.problemListRepository = problemListRepository;
        this.mongoTemplate = mongoTemplate;
        this.userRepository = userRepository;
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
            // 未登录只能看到公开题单
            return new ResponseEntity<>(problemListRepository.findByIsAccessible(true, pageable), HttpStatus.OK);
        }

        String userId = session.getAttribute("userId").toString();

        // 登录可以看到公开题单，用户创建题单以及用户所在题单
        return new ResponseEntity<>(problemListRepository
                .findByIsAccessibleOrCreatorOrUserListLike(
                        true, userId, userId, pageable), HttpStatus.OK);
    }

    /**
     * GET查看题单详情
     *
     * @param id      需要查看的题单的ID
     * @param session 请求会话
     * @return 是否获取成功
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

        // 是自己创建的题单
        if (tempProblemList.getCreator().equals(tempObjectId)) {
            return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
        }

        // 是公开题单或者是自己所在的题单
        if (tempProblemList.isAccessible() ||
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
     * POST创建题单
     *
     * @param problemList 待创建的题单
     * @param session     请求会话
     * @return 是否创建成功
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
        if (!problemList.isAccessible()) {
            problemList.setUserList(null);
        }

        String tempObjectId = session.getAttribute("userId").toString();

        // 题单创建者为当前登录的用户
        problemList.setCreator(tempObjectId);
        ProblemList tempProblemList = problemListRepository.save(problemList);

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
     * PUT更改题单信息
     *
     * @param problemList 更新后的题单信息
     * @param id          目标题单ID
     * @param session     请求会话
     * @return 是否修改成功
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modifyProblemList(@RequestBody @Valid ProblemList problemList,
                                            @PathVariable String id,
                                            HttpSession session) {
        String tempObjectId = session.getAttribute("userId").toString();
        ProblemList tempProblemList = problemListRepository.findById(id);

        // 鉴权
        if (!(isSignedIn(session)
                && tempObjectId.equals(tempProblemList.getCreator()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        // 题单中的四个日期是否合法
        if (isNotValidDate(problemList)) {
            return new ResponseEntity<>(new FieldsInvalidError(), HttpStatus.BAD_REQUEST);
        }

        // 题单是否为公开题单
        if (problemList.isAccessible()) {
            problemList.setUserList(null);
        }

        problemList.setId(id);
        problemList.setCreator(tempObjectId);

        return new ResponseEntity<>(problemListRepository.save(problemList), HttpStatus.OK);
    }

    /**
     * DELETE删除题单
     *
     * @param id      目标题单ID
     * @param session 请求会话
     * @return 是否删除成功
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblemList(@PathVariable String id,
                                            HttpSession session) {
        ProblemList tempProblemList = problemListRepository.findById(id);

        String tempObjectId = session.getAttribute("userId").toString();

        // 是否登录以及题单所有者是不是当前用户
        if (!(isSignedIn(session)
                && tempObjectId
                .equals(tempProblemList
                        .getCreator()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        problemListRepository.delete(tempProblemList);

        // 更新用户字段
        User user = userRepository.findById(tempObjectId);
        user.deleteProblemListOwned(tempProblemList.getId());
        userRepository.save(user);

        return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
    }

    /**
     * 判断题单中的四个时间字段是否合法
     *
     * @param problemList 需要进行合法判断的题单
     * @return true：日期合法，反之
     */
    private boolean isNotValidDate(ProblemList problemList) {
        Date readBeginTime;
        Date readEndTime = null;
        Date answerBeginTime;
        Date answerEndTime = null;

        /*
        * 1. readBeginTime < readEndTime
        * 2. answerBeginTime < answerEndTime
        * 3. readBeginTime < answerBeginTime
        * 4. answerEndTime < readEndTime
        * */
        return !((readBeginTime = problemList.getReadBeginTime()) != null &&
                (readEndTime = problemList.getReadEndTime()) != null &&
                readBeginTime.after(readEndTime)) &&
                !((answerBeginTime = problemList.getAnswerBeginTime()) != null &&
                        (answerEndTime = problemList.getAnswerEndTime()) != null &&
                        answerBeginTime.after(answerEndTime)) &&
                !((readBeginTime != null && answerBeginTime != null) &&
                        readBeginTime.after(answerBeginTime)) &&
                !((readEndTime != null && answerEndTime != null) &&
                        readEndTime.before(answerEndTime));
    }

}