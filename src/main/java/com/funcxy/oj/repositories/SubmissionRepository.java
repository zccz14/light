package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Submission;
import com.sun.corba.se.spi.ior.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by lqp on 2017/3/1.
 */
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    List<Submission> findById(ObjectId id);

    List<Submission> findByProblemListId(ObjectId problemlistid);

    List<Submission> findByProblemId(ObjectId problemid);

    List<Submission> findByStatus(String status);


    List<Submission> findBySentence(String sentence);

    List<Submission> findByContent(String content);

}
