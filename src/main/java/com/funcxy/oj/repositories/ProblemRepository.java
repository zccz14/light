package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Problem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by wtupc96 on 2017/2/28.
 */
public interface ProblemRepository extends MongoRepository<Problem, String> {
    public Problem findById(ObjectId id);
    public Problem findByCreatorId(ObjectId creatorId);
    public Problem findByTitle(String title);
    public Problem findByType(String type);
}
