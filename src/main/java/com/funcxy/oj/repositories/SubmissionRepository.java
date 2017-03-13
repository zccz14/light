package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author  by lqp on 2017/3/1.
 */
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    Submission findById(String id);

    List<Submission> findByUserId(String userId);

    List<Submission> findByProblemListId(String problemlistId);

    List<Submission> findByProblemId(String problemId);

    List<Submission> findByStatus(String status);

    List<Submission> findBySentence(String sentence);

    List<Submission> findByContent(String content);

    @Query("{'$and':[{'userId':?0},{'problemId':{'$in':?1}}]}")
    Page<Submission> roughFind(String userId, List<String> problemId, Pageable pageable);

    @Query("{'userId':{'$in':?0}}")
    Page<Submission> findByUserIds(List<String> userIds, Pageable pageable);

    @Query("{'userId': {'$in':?0}}")
    Page<Submission> findByProblemIds(List<String> problemIds, Pageable pageable);
}
