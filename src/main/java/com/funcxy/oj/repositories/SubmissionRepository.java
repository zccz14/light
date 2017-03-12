package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

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


    @Query("{'userId':?0, 'problemId':?1, 'problemListId':?2}")
    Page<Submission> roughFind(String userId, String problemId, String problemListId, Pageable pageable);

//    @Query("{'$and':[{userId}]}")
//    {$and:[{userId: '21312312321'}, {problemId: {$in: ['21321321', '1']}}]}

}
