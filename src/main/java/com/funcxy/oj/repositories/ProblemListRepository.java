package com.funcxy.oj.repositories;

import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by chenyu on 2017/3/1.
 *
 * Modified by wtupc96 on 2017/3/4
 */

public interface ProblemListRepository extends MongoRepository<ProblemList, ObjectId> {
    ProblemList findById(ObjectId id);

    @Query(fields = "{'title':1}")
    Page<ProblemList> findByCreatorLike(User user, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<ProblemList> findByTitleLike(String title, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<ProblemList> findByCreatorLikeAndTitleLike(User creator, String title, Pageable pageable);
}
