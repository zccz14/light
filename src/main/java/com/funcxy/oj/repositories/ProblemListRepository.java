package com.funcxy.oj.repositories;

import com.funcxy.oj.models.ProblemList;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by chenyu on 2017/3/1.
 */
public interface ProblemListRepository extends MongoRepository<ProblemList, ObjectId> {

}
