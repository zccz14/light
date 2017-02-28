package com.funcxy.oj.repositories;

import com.funcxy.oj.modules.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by DDHEE on 2017/2/28.
 */
public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByUsername(String username);
    List<User> findByUsernameLike (String username);
    List<User> findByEmail(String email);
}
