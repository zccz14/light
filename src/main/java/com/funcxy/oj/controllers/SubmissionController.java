package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.models.JudgeProblem;
import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.Submission;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.SubmissionRepository;
import com.funcxy.oj.repositories.UserRepository;
import com.funcxy.oj.utils.InvalidException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * Created by niyou2016 on 2017/3/2 0002.
 */
@RestController
@RequestMapping("/submission")

public class SubmissionController {
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ProblemListRepository problemListRepository;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity handInSubmission(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(submissionRepository.findById(id), HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity createSubmission(@Valid Submission submission, @PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        submission.setId(id);
        return new ResponseEntity<Object>(submissionRepository.save(submission), HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateSentence(@RequestBody @Valid Submission submission, @PathVariable ObjectId id, HttpSession session) {
        // UserId
        User theUser = userRepository.findById(id);
        ObjectId userId = theUser.getId();
        Submission theSubmission = submissionRepository.findById(id);
        ObjectId problemListId = theSubmission.getProblemListId();
        ProblemList problemList = problemListRepository.findById(problemListId);
        List<JudgeProblem> list = problemList.getJudgerList();
        ObjectId problemId = theSubmission.getProblemId();
        List<ObjectId> judger = list.stream()
                .filter(v -> v.getProblemId().equals(problemId))
                .map(v -> v.getJudgeId())
                .collect(Collectors.toList());
        if (judger == null) {
            return new ResponseEntity<>(new InvalidException("problem not found"), HttpStatus.NOT_FOUND);
        } else {
            ObjectId judgerId = judger.get(0);
            if (judgerId.equals(userId)) {
                return new ResponseEntity(submissionRepository.save(submission), HttpStatus.OK);
            } else {
                return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
            }
        }


    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity searchSubmission(@RequestBody @Valid Submission submission, @PathVariable ObjectId id, HttpSession session) {
        // if (!isSignedIn(session)) {
        // return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
        // }
        User theUser = userRepository.findById(id);
        ObjectId userId = theUser.getId();
        Submission theSubmission = submissionRepository.findById(id);
        ObjectId problemListId = theSubmission.getProblemListId();
        ProblemList problemList = problemListRepository.findById(problemListId);
        List<JudgeProblem> list = problemList.getJudgerList();
        ObjectId problemId = theSubmission.getProblemId();
        List<ObjectId> judger = list.stream()
                .filter(v -> v.getProblemId().equals(problemId))
                .map(v -> v.getJudgeId())
                .collect(Collectors.toList());
        if (judger == null) {
            return new ResponseEntity<>(new InvalidException("problem not found"), HttpStatus.NOT_FOUND);
        } else {
            ObjectId judgerId = judger.get(0);
            if (judgerId.equals(userId)) {
                Submission theSubmissionid = submissionRepository.findById(id);
                return new ResponseEntity(theSubmissionid, HttpStatus.OK);
            } else {
                return new ResponseEntity(new ForbiddenError(), HttpStatus.FORBIDDEN);
            }
        }

    }
}
