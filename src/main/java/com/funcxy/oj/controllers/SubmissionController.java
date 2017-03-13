package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.errors.NotFoundError;
import com.funcxy.oj.errors.TooEarlyError;
import com.funcxy.oj.errors.TooLateError;
import com.funcxy.oj.models.*;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.ProblemRepository;
import com.funcxy.oj.repositories.SubmissionRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * @author niyou2016 on 2017/3/2 0002.
 */
@RestController
@RequestMapping("/submission")

public class SubmissionController {
    private final SubmissionRepository submissionRepository;
    private final ProblemListRepository problemListRepository;
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;

    @Autowired
    public SubmissionController(SubmissionRepository submissionRepository, ProblemListRepository problemListRepository, UserRepository userRepository, ProblemRepository problemRepository) {
        this.submissionRepository = submissionRepository;
        this.problemListRepository = problemListRepository;
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
    }

    /**
     * 查找提交
     *
     * @param id      提交Id
     * @param session session
     * @return 成功时返回提交对象
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity retrieveSubmission(@PathVariable String id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Submission submission = submissionRepository.findById(id);
        if (submission == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(submission, HttpStatus.OK);
    }

    /**
     * 创建提交
     *
     * @param submission 提交对象
     * @param session    session对象
     * @return 成功时返回提交对象
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createSubmission(@Valid Submission submission, HttpSession session) {

        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        //TODO:鉴权
        User user = userRepository.findById(session.getAttribute("userId").toString());
        ProblemList problemList = problemListRepository.findById(submission.getProblemListId());
        Problem problem = problemRepository.findById(submission.getProblemId());

        if (user == null || problemList == null || problem == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        User judger = userRepository.findById(problemList.getJudgerList().get(problemList.getProblemIds().indexOf(problem.getId())));

        if (judger == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }

        user.addSubmissionHistory(submission.getId());
        userRepository.save(user);
        judger.addSubmissionUndicided(submission.getId());
////////////////////////
        //
        Date answerBeginTime = problemList.getAnswerBeginTime();
        Date answerEndTime = problemList.getAnswerEndTime();

        // 获得现在的时间
        Date now = new Date(System.currentTimeMillis());

        if (answerBeginTime.before(now) || answerBeginTime.equals(now)) {
            return new ResponseEntity<>(new TooEarlyError(), HttpStatus.FORBIDDEN);
        } else {
            if (answerEndTime.before(now)) {
                return new ResponseEntity<>(new TooLateError(), HttpStatus.FORBIDDEN);
            } else if (answerEndTime.after(now) || answerEndTime.equals(now)) {
                //加入Message
                //TODO:考虑读取配置文件，或保存配置名称
                judger.addMessage(new Message("Submission to judge",
                        "A submission by " + user.getUsername() + " to promblem" +
                                problem.getTitle() +
                                " has just been created , don't forget to decide in time.",
                        MessageType.TOJUDGE));
                userRepository.save(judger);
                return new ResponseEntity<>(submissionRepository.insert(submission), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new TooEarlyError(), HttpStatus.FORBIDDEN);
    }

    /**
     * 修改判决/处理提交
     *
     * @param submission   提交对象（包含判决）
     * @param submissionId 提交Id
     * @param session      session对象
     * @return 成功时返回新提交
     * 已判决的题目不可再标记为未判决
     */
    @RequestMapping(value = "/{submissionId}", method = RequestMethod.PUT)
    public ResponseEntity updateSentence(@RequestBody Submission submission, @PathVariable String submissionId,
                                         HttpSession session) {
        // UserId
        if (!UserUtil.isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        User user = userRepository.findById(session.getAttribute("userId").toString());
        Submission theSubmission = submissionRepository.findById(submissionId);
        String problemListId = theSubmission.getProblemListId();
        ProblemList problemList = problemListRepository.findById(problemListId);
        List<String> judgers = problemList.getJudgerList();
        String problemId = theSubmission.getProblemId();
        User judger = userRepository.findById(judgers.get(problemList.getProblemIds().indexOf(problemId)));
        if (judger == null) {
            return new ResponseEntity<>(new NotFoundError(), HttpStatus.NOT_FOUND);
        }
        if (!judger.getId().equals(user.getId())) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        if (judger.getId().equals(user.getId())) {
            theSubmission.setSentence(submission.getSentence());
            theSubmission.setStatus(SubmissionStatus.DECIDED);
            User submitter = userRepository.findById(theSubmission.getUserId());
            submitter.addMessage(
                    new Message(
                            "Sentence updated",
                            "Your submission to the problem " +
                                    problemRepository.findById(theSubmission.getProblemId()).getTitle() +
                                    "has just been sentenced. Don't forget to take a look.",
                            MessageType.SENTENCE
                    )
            );
            userRepository.save(submitter);
            return new ResponseEntity<>(submissionRepository.save(submission), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
    }

    /**
     * 搜索提交
     * @param session session
     * @return 成功时返回
     */
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public ResponseEntity searchSubmission(@RequestParam(defaultValue = "") String username,
//                                           @RequestParam(defaultValue = "") String problemTitle,
//                                           Pageable pageable,
//                                           @RequestParam  HttpSession session) {
//        if (!isSignedIn(session)) {
//            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
//        }
//
//        User user = userRepository.findOneByUsername(username);
//        List<Problem> problems = problemRepository.findByTitleLike(problemTitle);
//        List<Submission> submissionIdList = users.stream().map(User::getId)
//                .map(submissionRepository::findByUserId).reduce();
////                        problems.stream().map(Problem::getId).map(submissionRepository::findByProblemId).collect(Collectors.toList()));
//        Page<Submission> submissions = new PageImpl<Submission>(
//                ,
//                pageable,
//
//        )
//
//        return new ResponseEntity<>(submissionRepository.findById(submission.getId()), HttpStatus.OK);
//    }
}
