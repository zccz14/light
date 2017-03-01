package com.funcxy.oj.repositories;

import com.funcxy.oj.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.stream.Stream;

/**
 * User DAO
 * @author ddhee
 */
public interface UserRepository extends MongoRepository<User, String> {
    Stream<User> findById(ObjectId id);
    List<User> findByUsername(String username);
    List<User> findByUsernameLike (String username);
    List<User> findByEmail(String email);
}
