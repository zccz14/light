package com.funcxy.oj.repositories;

import com.funcxy.oj.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * User DAO
 * @author ddhee
 */
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    User findByUsernameLike (String username);
    User findByEmail(String email);
    User findOneByUsername(String username);
}
