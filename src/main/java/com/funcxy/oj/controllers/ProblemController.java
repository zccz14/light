package com.funcxy.oj.controllers;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.services.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wtupc96 on 2017/2/28.
 */

@RestController
@RequestMapping("/problem")
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(method = RequestMethod.POST)
    public Problem saveProblem(Problem problem) throws Exception {
        return problemService.save(problem);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Problem> getProblem(Problem problem){
        return problemService.find(problem);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Problem updateProblem(Problem problem){
        return problemService.update(problem);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Problem deleteProblem(Problem problem){
        return problemService.delete(problem);
    }
}
