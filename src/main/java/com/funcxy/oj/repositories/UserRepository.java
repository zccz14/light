package com.funcxy.oj.repositories;

import com.funcxy.oj.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * User DAO
 * @author ddhee
 */
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String username);
    List<User> findByUsernameLike (String username);
    List<User> findByEmail(String email);
}
