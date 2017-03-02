package com.funcxy.oj.services;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.models.User;
import com.funcxy.oj.repositories.ProblemRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by wtupc96 on 2017/2/28.
 */

@Service
public class ProblemService {
    @Autowired
    ProblemRepository problemRepository;


    public Problem save(@Valid Problem problem) {
        problem.setTitle(problem.getTitle().trim());
        problem.setDescription(problem.getDescription().trim());
        if (problem.getReferenceAnswer() != null)
            problem.setReferenceAnswer(problem.getReferenceAnswer().trim());
        problem.setType(problem.getType().trim());
        return problemRepository.save(problem);
    }

    public Problem delete(ObjectId objectId) {
        Problem tempProblem = problemRepository.findById(objectId);
        problemRepository.delete(objectId);
        return tempProblem;
    }

    public Problem delete(Problem problem) {
        return delete(problem.getId());
    }

    public Problem update(Problem newProblem) {
        return problemRepository.save(newProblem);
    }

    public Problem findById(ObjectId id) {
        return problemRepository.findById(id);
    }

    public List<Problem> findByCreator(User creator) {
        return problemRepository.findByCreator(creator);
    }

    public List<Problem> findByType(String type) {
        return problemRepository.findByType(type);
    }

    public List<Problem> findByTitle(String title) {
        return problemRepository.findByTitle(title);
    }


//    // retainAll方法仅比较引用。
//    public List<Problem> find(Problem problem) {
//        List<Problem> problemList = new ArrayList<>();
//        if (problem.getType() != null)
//            problemList.addAll(findByType(problem.getType()));
//        System.out.println(problemList);
//        if (problem.getTitle() != null)
//            problemList.retainAll(findByTitle(problem.getTitle()));
//        System.out.println(problemList);
//        if (problem.getCreator() != null)
//            problemList.addAll(findByCreator(problem.getCreator()));
//        return problemList;
//    }
}
