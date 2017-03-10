package com.funcxy.oj.repositories;

import com.funcxy.oj.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * User DAO
 * @author ddhee
 */
public interface UserRepository extends MongoRepository<User, String> {
    User findById(String id);
    List<User> findByUsernameLike (String username);
    User findOneByEmail(String email);
    User findOneByUsername(String username);
    List<User> findByEmail(String email);
    List<User> findByUsername(String username);
    @Query("{'profile':{'location':?0}}")
    List<User> findByProfile_Location(String location);
    @Query("{'profile':{'nickname':?0}}")
    List<User> findByProfile_NicknameLike(String nickname);
    @Query("{'profile':{'bio':?0}}")
    List<User> findByProfile_BioLike(String bio);

    @Query("{'username':{ '$regex':?0}, 'email':{ '$regex':?1}}, 'profile': {'nickname': {'$regex':?2}, 'bio': {'$regex':?3}, 'location':{'$regex':?4}}}")
    Page<User> roughFind(String username, String email, String nickname, String bio, String location, Pageable pageable);
}
