package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by wtupc96 on 2017/2/28.
 */
public interface ProblemRepository extends MongoRepository<Problem, ObjectId> {
    public Problem findById(ObjectId id);
    public List<Problem> findByCreator(User creator);
    public List<Problem> findByTitle(String title);
    public List<Problem> findByType(String type);

    @Query(value = "{?0:?1}", fields = "{'title':1}")
    public List<Problem> findByTheArg(String arg, Object argValue);
}
