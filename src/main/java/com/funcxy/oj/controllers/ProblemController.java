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
    public ResponseEntity<Problem> saveProblem(@Valid Problem problem, HttpSession session) {
        return new ResponseEntity<>(problemRepository.save(problem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Problem> getOneSpecificProblem(@PathVariable ObjectId id, HttpSession session) {
        return new ResponseEntity<Problem>(problemRepository.findById(id), HttpStatus.OK);
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
    public ResponseEntity<List<Object>> getProblem(Problem problem, HttpSession session) {
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
                new ResponseEntity<List<Object>>
                        (problemIdList
                                .stream()
                                .map(pro ->
                                        new cleanedProblem(pro.getId(), pro.getTitle()))
                                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Problem> updateProblem(@RequestBody @Valid Problem problem, @PathVariable ObjectId id, HttpSession session) {
        Problem tempProblem = problemRepository.findById(id);
        if (problem.getReferenceAnswer() != null) {
            problem.setReferenceAnswer(tempProblem.getReferenceAnswer());
        }
        problem.setId(id);
        return new ResponseEntity<Problem>(problemRepository.save(problem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Problem> deleteProblem(@PathVariable ObjectId id, HttpSession session) {
        Problem tempProblem = problemRepository.findById(id);
        problemRepository.delete(tempProblem);
        return new ResponseEntity<Problem>(tempProblem, HttpStatus.OK);
    }
}
