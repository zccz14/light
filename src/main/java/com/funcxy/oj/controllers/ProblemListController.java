package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.BadRequestError;
import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.models.CleanedProblemList;
import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemListRepository;
import com.funcxy.oj.repositories.UserRepository;
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
 * Created by wtupc96 on 2017/3/4.
 *
 * @author Peter
 * @version 1.0
 */

@RestController
@RequestMapping("/problemLists")
public class ProblemListController {
    @Autowired
    private ProblemListRepository problemListRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblemLists(HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        List<ProblemList> problemLists = problemListRepository.findAll();
        return
                new ResponseEntity<>(problemLists
                        .stream()
                        .map(problemList -> new CleanedProblemList(problemList.getId(), problemList.getTitle(),problemList.getType()))
                        .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblemList(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        ProblemList tempProblemList = problemListRepository.findById(id);

        if (tempProblemList == null) {
            return new ResponseEntity<>(new BadRequestError(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createProblemList(@Valid ProblemList problemList, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }

        ProblemList tempProblemList = problemListRepository.save(problemList);
        User user = userRepository.findById(new ObjectId(session.getAttribute("userId").toString()));
        user.addProblemListOwned(tempProblemList.getId());
        userRepository.save(user);
        return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity modifyProblemList(@RequestBody @Valid ProblemList problemList, @PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
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
        return new ResponseEntity<>(problemListRepository.save(problemList), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblemList(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        ProblemList tempProblemList = problemListRepository.findById(id);
        problemListRepository.delete(tempProblemList);

        User user = userRepository.findById(new ObjectId( session.getAttribute("userId").toString()));
        user.deleteProblemListOwned(tempProblemList.getId());
        userRepository.save(user);
        return new ResponseEntity<>(tempProblemList, HttpStatus.OK);
    }
}
