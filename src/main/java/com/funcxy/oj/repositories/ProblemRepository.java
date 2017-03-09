package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by wtupc96 on 2017/2/28.
 *
 * @author Peter
 * @version 1.0
 */
public interface ProblemRepository extends MongoRepository<Problem, ObjectId> {
    Problem findById(ObjectId id);

    @Query(value = "{'creator':?0}", fields = "{'title':1}")
    Page<Problem> getAllProblems(ObjectId creator, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<Problem> findByCreatorLike(ObjectId creator, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<Problem> findByTitleLike(String title, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<Problem> findByTypeLike(String type, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<Problem> findByTypeLikeAndTitleLike(String type, String title, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<Problem> findByTypeLikeAndCreatorLike(String type, User creator, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<Problem> findByCreatorLikeAndTitleLike(User user, String title, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<Problem> findByTypeLikeAndTitleLikeAndCreatorLike(String type, String title, User creator, Pageable pageable);

    @Query(value = "{?0:?1}", fields = "{'title':1}")
    Page<Problem> findByTheArg(String arg, Object argValue, Pageable pageable);
}
