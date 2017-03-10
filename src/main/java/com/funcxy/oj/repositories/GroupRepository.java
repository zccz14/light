package com.funcxy.oj.repositories;
import com.funcxy.oj.models.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

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
}
