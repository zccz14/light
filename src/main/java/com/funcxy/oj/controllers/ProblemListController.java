package com.funcxy.oj.controllers;

import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.repositories.ProblemListRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * Created by wtupc96 on 2017/3/4.
 */

@RequestMapping("/problemLists")
public class ProblemListController {
    @Autowired
    private ProblemListRepository problemListRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblemLists(HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        List<ProblemList> problemLists = problemListRepository.findAll();

        class innerTempClass {
            public ObjectId id;
            public String title;

            public innerTempClass(ObjectId id, String title) {
                this.id = id;
                this.title = title;
            }
        }

        return
                new ResponseEntity(problemLists
                        .stream()
                        .map(problemList -> new innerTempClass(problemList.getId(), problemList.getTitle()))
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblemList(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(problemListRepository.findById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createProblemList(@Valid ProblemList problemList, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity(problemListRepository.save(problemList), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modifyProblemList(@RequestBody @Valid ProblemList problemList, @PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
//        if(problemList.getType() != null);
//        if(problemList.getTitle() != null);
//        if(problemList.getAnswerBeginTime() != null);
//        if(problemList.getAnswerEndTime() != null);
//        if(problemList.getReadBeginTime() != null);
//        if(problemList.getReadEndTime() != null);
//        if(problemList.getJudgerList() != null);
//        if(problemList.getProblemIds() != null);
//        if(problemList.getUserList() != null);
        problemList.setId(id);
        return new ResponseEntity(problemListRepository.save(problemList), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblemList(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        ProblemList tempProblemList = problemListRepository.findById(id);
        problemListRepository.delete(tempProblemList);
        return new ResponseEntity(tempProblemList, HttpStatus.OK);
    }
}
