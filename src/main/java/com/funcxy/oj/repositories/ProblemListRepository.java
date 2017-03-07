package com.funcxy.oj.repositories;

import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by chenyu on 2017/3/1.
 *
 * Modified by wtupc96 on 2017/3/4
 */

public interface ProblemListRepository extends MongoRepository<ProblemList, ObjectId> {
    public List<ProblemList> findByCreator(User user);
    public ProblemList findById(ObjectId id);
}
