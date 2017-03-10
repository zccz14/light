package com.funcxy.oj.services;

import com.funcxy.oj.models.Submission;
import com.funcxy.oj.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by lqp on 2017/3/3.
 */
@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SubmissionService(SubmissionRepository submissionRepository, MongoTemplate mongoTemplate) {
        this.submissionRepository = submissionRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public Submission save(@Valid Submission submission) {
        return submissionRepository.save(submission);
    }

    public Submission findById(String id) {
        return submissionRepository.findById(id);
    }

    public List<Submission> findByProblemId(String problemId) {
        return submissionRepository.findByProblemId(problemId);
    }

    public List<Submission> findByProblemListId(String problemListId) {
        return submissionRepository.findByProblemListId(problemListId);
    }

}
