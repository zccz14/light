package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Submission;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by lqp on 2017/3/1.
 */
public interface SubmissionRepository extends MongoRepository<Submission, ObjectId> {
    Submission findById(ObjectId id);

    List<Submission> findByUserId(ObjectId userId);

    List<Submission> findByProblemListId(ObjectId problemlistId);

    List<Submission> findByProblemId(ObjectId problemId);

    List<Submission> findByStatus(String status);

    List<Submission> findBySentence(String sentence);

    List<Submission> findByContent(String content);

}
