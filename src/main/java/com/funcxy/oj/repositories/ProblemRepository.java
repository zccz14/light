package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Problem;
import com.funcxy.oj.models.User;
import org.springframework.data.domain.Page;
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
public interface ProblemRepository extends MongoRepository<Problem, String> {
    /**
     * 根据ID查询某一个问题
     *
     * @param id 目标问题的ID
     * @return 问题
     */
    Problem findById(String id);

    /**
     * 查询某用户创建的所有题目
     *
     * @param creator  创建者
     * @param pageable 分页参数
     * @return 该用户创建的所有问题
     */
    @Query(value = "{'creator':?0}", fields = "{'description': -1}")
    Page<Problem> getAllProblems(String creator, Pageable pageable);

    @Query(fields = "{'title':1}")
    Page<Problem> findByCreatorLike(String creator, Pageable pageable);

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

    List<Problem> findByTitleLike(String title);
}
