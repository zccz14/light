package com.funcxy.oj.services;

import com.funcxy.oj.models.Submission;
import com.funcxy.oj.repositories.SubmissionRepository;
import com.sun.corba.se.spi.ior.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by lqp on 2017/3/3.
 */
@Service
public class SubmissionService {
    @Autowired
    SubmissionRepository submissionRepository;

    public Submission save(@Valid Submission submission) {
        return submissionRepository.save(submission);
    }

    public Submission findById(ObjectId id) {
        return submissionRepository.findById(id);
    }

    public List<Submission> findByProblemId(ObjectId problemId) {
        return submissionRepository.findByProblemId(problemId);
    }

    public List<Submission> findByProblemListId(ObjectId problemListId) {
        return submissionRepository.findByProblemListId(problemListId);
    }

}
