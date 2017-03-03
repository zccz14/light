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
    Stream<User> findByUserId(ObjectId userId);

    List<User> findByUsernameLike (String username);

    User findOneByEmail(String email);

    User findOneByUsername(String username);

    List<User> findByLocation(String location);
}
