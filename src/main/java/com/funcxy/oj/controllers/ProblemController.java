package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.BadRequestError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.models.Problem;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.DataPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * Created by wtupc96 on 2017/2/28.
 *
 * @author Peter
 * @version 1.0
 */

@RestController
@RequestMapping("/problems")
public class ProblemController {
    /**
     * 默认排序方式
     */
    private static final Sort sort = new Sort(Sort.Direction.ASC, "title");

    /**
     * problems数据库操作
     */
    private final ProblemRepository problemRepository;

    /**
     * users数据库操作
     */
    private final UserRepository userRepository;

    private final MongoTemplate mongoTemplate;

    /**
     * 查询分页参数
     */
    private DataPageable pageable;

    {
        pageable = new DataPageable();
        pageable.setSort(sort);
    }

    @Autowired
    public ProblemController(ProblemRepository problemRepository, UserRepository userRepository, MongoTemplate mongoTemplate) {
        this.problemRepository = problemRepository;
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * POST创建题目
     *
     * @param problem 将要创建的题目
     * @param session 请求会话
     * @return 是否创建成功
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveProblem(@RequestBody @Valid Problem problem,
                                      HttpSession session) {
        // 鉴权
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        // 将题目创建者设置为当前登录用户
        problem.setCreator(session.getAttribute("userId").toString());

        Problem tempProblem = problemRepository.save(problem);
        User user = userRepository.findById(session.getAttribute("userId").toString());

        user.addProblemOwned(tempProblem.getId());
        userRepository.save(user);
        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }


    /**
     * GET查看题目详情
     *
     * @param id      要查看的题目的ID
     * @param session 请求会话
     * @return 题目详情
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblem(@PathVariable String id,
                                                HttpSession session) {
        // 鉴权
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        Problem tempProblem = problemRepository.findById(id);

        // 如果没有这个题目
        if (tempProblem == null) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        // 如果题目的创建者不是当前登录用户
        if (!(tempProblem.getCreator()
                .equals(session.getAttribute("userId").toString()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }


    /**
     * GET查看自己创建的题目
     *
     * @param pageNumber 页码
     * @param pageSize 页大小
     * @param session 请求会话
     * @return 所有自己创建的题目
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblem(@RequestParam int pageNumber,
                                     @RequestParam int pageSize,
                                     HttpSession session) {
        // 鉴权
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        // 设置分页参数
        pageable.setPageNumber(pageNumber);
        pageable.setPageSize(pageSize);

        /*后端检索问题功能，版权所有，请勿删除。
        String type = problem.getType();
        String title = problem.getTitle();
        User creator = problem.getCreator();

        if (type != null && title != null && creator != null) {
            return new ResponseEntity<>(problemRepository.findByTypeLikeAndTitleLikeAndCreatorLike(type, title, creator, pageable), HttpStatus.OK);
        } else if (type != null && title != null) {
            return new ResponseEntity<>(problemRepository.findByTypeLikeAndTitleLike(type, title, pageable), HttpStatus.OK);
        } else if (type != null && creator != null) {
            return new ResponseEntity<>(problemRepository.findByTypeLikeAndCreatorLike(type, creator, pageable), HttpStatus.OK);
        } else if (title != null && creator != null) {
            return new ResponseEntity<>(problemRepository.findByCreatorLikeAndTitleLike(creator, title, pageable), HttpStatus.OK);
        } else if (type != null) {
            return new ResponseEntity<>(problemRepository.findByTypeLike(type, pageable), HttpStatus.OK);
        } else if (title != null) {
            return new ResponseEntity<>(problemRepository.findByTitleLike(title, pageable), HttpStatus.OK);
        } else if (creator != null) {
            return new ResponseEntity<>(problemRepository.findByCreatorLike(creator, pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(problemRepository.findAll(pageable), HttpStatus.OK);
        }*/

        // 返回所有自己创建的题目
        return new ResponseEntity<>(problemRepository
                .getAllProblems(session
                        .getAttribute("userId")
                        .toString(), pageable), HttpStatus.OK);
    }

    /**
     * PUT修改某一个题目
     *
     * @param problem 需要进行修改的题目
     * @param id 目标题目的ID
     * @param session 请求会话
     * @return 修改之后的结果
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateProblem(@RequestBody @Valid Problem problem,
                                        @PathVariable String id,
                                        HttpSession session) {
        // 鉴权
        if (!(isSignedIn(session) &&
                session.getAttribute("userId")
                        .toString()
                        .equals(problem
                                .getCreator()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        problem.setId(id);
        return new ResponseEntity<Object>(problemRepository.save(problem), HttpStatus.OK);
    }

    /**
     * DELETE删除某一个题目
     *
     * @param id 目标题目的ID
     * @param session 请求会话
     * @return 删除的题目
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblem(@PathVariable String id,
                                        HttpSession session) {
        // 鉴权
        if (!(isSignedIn(session) &&
                session.getAttribute("userId")
                        .toString()
                        .equals(problemRepository
                                .findById(id)
                                .getCreator()))) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Problem tempProblem = problemRepository.findById(id);

        // 如果没有这个题目
        if (tempProblem == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }

        problemRepository.delete(tempProblem);

        // 更新user的字段
        User user = userRepository.findById(session.getAttribute("userId").toString());
        user.deleteProblemOwned(tempProblem.getId());
        userRepository.save(user);
        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }
}
