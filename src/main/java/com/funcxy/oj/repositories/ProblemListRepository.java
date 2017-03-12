package com.funcxy.oj.repositories;

import com.funcxy.oj.models.ProblemList;
import com.funcxy.oj.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by chenyu on 2017/3/1.
 * <p>
 * Modified by wtupc96 on 2017/3/4
 */

public interface ProblemListRepository extends MongoRepository<ProblemList, String> {
    ProblemList findById(String id);

    @Query(value = "{'creator':?0}", fields = "{'title':1,'creator':1}")
    Page<ProblemList> getAllProblemListsCreated(String creator, Pageable pageable);

    @Query(fields = "{'title':1,'creator':1}")
    Page<ProblemList> findByUserListLike(String userId, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<ProblemList> findByCreatorLike(User user, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<ProblemList> findByTitleLike(String title, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<ProblemList> findByCreatorLikeAndTitleLike(User creator, String title, Pageable pageable);

    ProblemList findOneByTitle(String title);
}
