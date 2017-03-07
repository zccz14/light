package com.funcxy.oj.controllers;

import com.funcxy.oj.errors.ForbiddenError;
import com.funcxy.oj.models.Problem;
import com.funcxy.oj.repositories.ProblemRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity saveProblem(@Valid Problem problem, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(problemRepository.save(problem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity getOneSpecificProblem(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(problemRepository.findById(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getProblem(Problem problem, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        List<Problem> problemIdList = null;
        if (problem.getType() != null) {
//            problemIdList = problemRepository.findByTheArg("type", problem.getType());
            problemIdList = problemRepository.findByTypeLike(problem.getType());
        }
        if (problem.getTitle() != null) {
            if (problemIdList == null) {
//                problemIdList = problemRepository.findByTheArg("title", problem.getTitle());
                problemIdList = problemRepository.findByTitleLike(problem.getTitle());
            } else {
//                problemIdList.retainAll(problemRepository.findByTheArg("title", problem.getTitle()));
                problemIdList.retainAll(problemRepository.findByTitleLike(problem.getTitle()));
            }
        }
        if (problem.getCreator() != null) {
            if (problemIdList == null) {
//                problemIdList = problemRepository.findByTheArg("creator", problem.getCreator());
                problemIdList = problemRepository.findByCreatorLike(problem.getCreator());
            } else {
//                problemIdList.retainAll(problemRepository.findByTheArg("creator", problem.getCreator()));
                problemIdList.retainAll(problemRepository.findByCreatorLike(problem.getCreator()));
            }
        }

        if (problemIdList == null) {
            problemIdList = problemRepository.findAll();
        }

        class cleanedProblem implements Serializable {
            public ObjectId id;
            public String title;

            public cleanedProblem(ObjectId id, String title) {
                this.id = id;
                this.title = title;
            }
        }

        return
                new ResponseEntity<>
                        (problemIdList
                                .stream()
                                .map(pro ->
                                        new cleanedProblem(pro.getId(), pro.getTitle()))
                                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity updateProblem(@RequestBody @Valid Problem problem, @PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
//        Problem tempProblem = problemRepository.findById(id);
//        if (problem.getReferenceAnswer() == null) {
//            problem.setReferenceAnswer(tempProblem.getReferenceAnswer());
//        }
        problem.setId(id);
        return new ResponseEntity<Object>(problemRepository.save(problem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteProblem(@PathVariable ObjectId id, HttpSession session) {
        if (!isSignedIn(session)) {
            return new ResponseEntity<>(new ForbiddenError(), HttpStatus.FORBIDDEN);
        }
        Problem tempProblem = problemRepository.findById(id);
        problemRepository.delete(tempProblem);
        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }
}
