package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by lqp on 2017/3/1.
 */
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    Submission findById(String id);

    List<Submission> findByUserId(String userId);

    List<Submission> findByProblemListId(String problemlistId);

    List<Submission> findByProblemId(String problemId);

    List<Submission> findByStatus(String status);

    List<Submission> findBySentence(String sentence);

    List<Submission> findByContent(String content);

}
