package com.funcxy.oj.controllers;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.funcxy.oj.utils.UserUtil.isSignedIn;

/**
 * Created by wtupc96 on 2017/2/28.
 */

@RestController
@RequestMapping("/problems")
public class ProblemController {
    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Object> saveProblem(@Valid Problem problem, HttpSession session) {
        if(!isSignedIn(session))
            return new ResponseEntity<>(new Error(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(problemRepository.save(problem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getOneSpecificProblem(@PathVariable ObjectId id, HttpSession session) {
        if(!isSignedIn(session))
            return new ResponseEntity<>(new Error(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(problemRepository.findById(id), HttpStatus.OK);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public List<Problem> getProblem(Problem problem){
//        List<Problem> problemList = new ArrayList<>();
//        if (problem.getType() != null)
//            problemList.addAll(problemRepository.findByType(problem.getType()));
//        if (problem.getTitle() != null)
//            if (problemList.isEmpty())
//                problemList.addAll(problemRepository.findByTitle(problem.getTitle()));
//            else
//                problemList.retainAll(problemRepository.findByTitle(problem.getTitle()));
//        if (problem.getCreator() != null)
//            if (problemList.isEmpty())
//                problemList.addAll(problemRepository.findByCreator(problem.getCreator()));
//            else
//                problemList.addAll(problemRepository.findByCreator(problem.getCreator()));
//        return problemList;
//    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Object> getProblem(Problem problem, HttpSession session) {
        if(!isSignedIn(session))
            return new ResponseEntity<>(new Error(), HttpStatus.FORBIDDEN);
        List<Problem> problemIdList = new ArrayList<>();
        if (problem.getType() != null) {
            problemIdList.addAll(problemRepository.findByTheArg("type", problem.getType()));
        }
        if (problem.getTitle() != null) {
            if (problemIdList.isEmpty()) {
                problemIdList.addAll(problemRepository.findByTheArg("title", problem.getTitle()));
            } else {
                problemIdList.retainAll(problemRepository.findByTheArg("title", problem.getTitle()));
            }
        }
        if (problem.getCreator() != null) {
            if (problemIdList.isEmpty()) {
                problemIdList.addAll(problemRepository.findByTheArg("creator", problem.getCreator()));
            }
            else {
                problemIdList.addAll(problemRepository.findByTheArg("creator", problem.getCreator()));
            }
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
    public ResponseEntity<Object> updateProblem(@RequestBody @Valid Problem problem, @PathVariable ObjectId id, HttpSession session) {
        if(!isSignedIn(session))
            return new ResponseEntity<>(new Error(), HttpStatus.FORBIDDEN);
        Problem tempProblem = problemRepository.findById(id);
        if (problem.getReferenceAnswer() != null) {
            problem.setReferenceAnswer(tempProblem.getReferenceAnswer());
        }
        problem.setId(id);
        return new ResponseEntity<Object>(problemRepository.save(problem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteProblem(@PathVariable ObjectId id, HttpSession session) {
        if(!isSignedIn(session))
            return new ResponseEntity<>(new Error(), HttpStatus.FORBIDDEN);
        Problem tempProblem = problemRepository.findById(id);
        problemRepository.delete(tempProblem);
        return new ResponseEntity<>(tempProblem, HttpStatus.OK);
    }
}
