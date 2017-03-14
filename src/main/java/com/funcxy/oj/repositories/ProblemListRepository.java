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
 *
 * @author Peter
 * @version 1.0
 */

public interface ProblemListRepository extends MongoRepository<ProblemList, String> {
    ProblemList findById(String id);

    @Query(value = "{'creator':?0}", fields = "{'coverUrl':1, 'title':1, 'creator':1}")
    Page<ProblemList> getAllProblemListsCreated(String creator, Pageable pageable);

    @Query(fields = "{'coverUrl':1, 'title':1, 'creator':1}")
    Page<ProblemList> findByUserListLike(String userId, Pageable pageable);

    @Query(fields = "{'coverUrl':1, 'title':1, 'creator':1}")
    Page<ProblemList> findByCreatorLike(User user, Pageable pageable);

    @Query(fields = "{'coverUrl':1, 'title':1, 'creator':1}")
    Page<ProblemList> findByTitleLike(String title, Pageable pageable);

    @Query(fields = "{'coverUrl':1, 'title':1, 'creator':1}")
    Page<ProblemList> findByCreatorLikeAndTitleLike(User creator, String title, Pageable pageable);

    /**
     * @param isPublic 题单公开性
     * @param pageable 分页参数
     * @return 查询结果
     */
    @Query(fields = "{'coverUrl':1, 'title':1, 'creator':1}")
    Page<ProblemList> findByIsPublic(boolean isPublic, Pageable pageable);

    /**
     * @param isPublic 题单公开性
     * @param creator  创建者
     * @param userId   题单包含的用户
     * @param pageable 分页参数
     * @return 查询结果
     */
    @Query(fields = "{ 'coverUrl':1, 'title':1, 'creator':1}")
    Page<ProblemList> findByIsPublicOrCreatorOrUserListLike(boolean isPublic, String creator, String userId, Pageable pageable);
}
