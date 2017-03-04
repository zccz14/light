package com.funcxy.oj.controllers;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.repositories.ProblemRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
    public Problem saveProblem(@Valid Problem problem) {
        return problemRepository.save(problem);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Problem getOneSpecificProblem(@PathVariable ObjectId id) {
        return problemRepository.findById(id);
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
    public List<Problem> getProblem(Problem problem) {
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

        return problemIdList;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Problem updateProblem(@RequestBody @Valid Problem problem, @PathVariable ObjectId id) {
        Problem tempProblem = problemRepository.findById(id);
        if (problem.getReferenceAnswer() != null) {
            problem.setReferenceAnswer(tempProblem.getReferenceAnswer());
        }
        problem.setId(id);
        return problemRepository.save(problem);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Problem deleteProblem(@PathVariable ObjectId id) {
        Problem tempProblem = problemRepository.findById(id);
        problemRepository.delete(tempProblem);
        return tempProblem;
    }
}
