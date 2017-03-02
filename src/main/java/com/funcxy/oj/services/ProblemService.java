package com.funcxy.oj.services;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.repositories.ProblemRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * Created by wtupc96 on 2017/2/28.
 */

@Service
public class ProblemService {
    @Autowired
    ProblemRepository problemRepository;

    private static boolean isNotValid(Problem problem) {
        return problem.getTitle().trim().equals("") ||
                problem.getDescription().trim().equals("") ||
                problem.getType().trim().equals("") ||
                problem.getReferenceAnswer().trim().equals("");
    }

    public Problem save(@Valid Problem problem) throws Exception {
        if (isNotValid(problem))
            throw new Exception("Problem's information is invalid");
        problem.setTitle(problem.getTitle().trim());
        problem.setDescription(problem.getDescription().trim());
        problem.setReferenceAnswer(problem.getReferenceAnswer().trim());
        problem.setType(problem.getType().trim());
        return problemRepository.save(problem);
    }

    public Problem delete(ObjectId objectId){
        Problem tempProblem = problemRepository.findById(objectId);
        problemRepository.delete(objectId.toString());
        return tempProblem;
    }

    public Problem delete(Problem problem){
        return delete(problem.getId());
    }

    public Problem update(Problem formerProblem, Problem newProblem){
        return update(formerProblem.getId(), newProblem);
    }

    public Problem update(ObjectId formerObjectId, Problem newProblem){
        problemRepository.delete(formerObjectId.toString());
        return problemRepository.save(newProblem);
    }
}
