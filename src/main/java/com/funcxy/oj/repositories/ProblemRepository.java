package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by wtupc96 on 2017/2/28.
 *
 * @author Peter
 * @version 1.0
 */
public interface ProblemRepository extends MongoRepository<Problem, ObjectId> {
    Problem findById(ObjectId id);

    @Query(fields = "{'title':1}")
    List<Problem> findByCreatorLike(User creator, Pageable pageable);

    @Query(fields = "{'title':1}")
    List<Problem> findByTitleLike(String title, Pageable pageable);

    @Query(fields = "{'title':1}")
    List<Problem> findByTypeLike(String type, Pageable pageable);

    @Query(value = "{?0:?1}", fields = "{'title':1}")
    List<Problem> findByTheArg(String arg, Object argValue, Pageable pageable);
}
