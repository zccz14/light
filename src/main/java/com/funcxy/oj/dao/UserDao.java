package com.funcxy.oj.dao;

import com.funcxy.oj.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
//import org.springframework.stereotype.Repository;

/**
 * Created by aak12 on 2017/2/28.
 */
public interface UserDao extends MongoRepository<User,ObjectId> {
    public List<User> findById(ObjectId id);
    public List<User> findByUsername(String username);
    public List<User> findByEmail(String email);

}
