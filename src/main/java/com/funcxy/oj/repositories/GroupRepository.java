package com.funcxy.oj.repositories;
import com.funcxy.oj.models.Group;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import javax.servlet.SessionTrackingMode;
import java.util.stream.Stream;

/**
 * @author  aak1247.
 */
public interface GroupRepository extends MongoRepository<Group,ObjectId>{
    Group findById(ObjectId id);
    Group findOneByGroupName(String name);
    Stream<Group> findByOwnerId(ObjectId ownerId);
    Stream<Group> findByGroupName(String name);
    Stream<Group> findByGroupNameLike(String name);
    @Query("{ 'groupName' : { 'regex':?0}, 'type': {'regex':?1}}")
    Page<Group> roughFind(String groupName, String type, Pageable pageable);
}
