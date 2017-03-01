package com.funcxy.oj.services;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.repositories.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                problem.getReferenceAnswer().trim().equals("") ||
                problem.getTitle() == null ||
                problem.getCreatorId() == null ||
                problem.getDescription() == null ||
                problem.getType() == null;
    }

    public Problem save(Problem problem) throws Exception {
        if (isNotValid(problem))
            throw new Exception("Problem's information is invalid");
        problem.setTitle(problem.getTitle().trim());
        problem.setDescription(problem.getDescription().trim());
        problem.setReferenceAnswer(problem.getReferenceAnswer().trim());
        problem.setType(problem.getType().trim());
        return problemRepository.save(problem);
    }
}
