package com.funcxy.oj.controllers;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.services.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wtupc96 on 2017/2/28.
 */

@RestController
@RequestMapping("/")
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("/addProblem")
    public Problem save(Problem problem) throws Exception {
        return problemService.save(problem);
    }

//    @RequestMapping("/findByTitle")
//    public List<Problem> findByName(String title){
//        return problemService.findByTitle(title);
//    }
}
