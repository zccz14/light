package com.funcxy.oj.controllers;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by wtupc96 on 2017/2/28.
 */

@RestController
@RequestMapping("/")
public class ProblemController {
    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping("/addProblem")
    public Problem save(Problem problem){
        return problemRepository.save(problem);
    }

    @RequestMapping("/findByTitle")
    public List<Problem> findByName(String title){
        return problemRepository.findByTitle(title);
    }
}
