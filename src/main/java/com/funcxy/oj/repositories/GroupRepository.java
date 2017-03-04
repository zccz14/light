package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by aak12 on 2017/3/4.
 */
public interface GroupRepository extends MongoRepository<Group,ObjectId>{
}
