package com.funcxy.oj.repositories;
import com.funcxy.oj.models.Group;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.servlet.SessionTrackingMode;
import java.util.stream.Stream;

/**
 * Created by aak12 on 2017/3/4.
 */
public interface GroupRepository extends MongoRepository<Group,ObjectId>{
    Group findById(ObjectId id);
    Group findOneByName(String name);
    Stream<Group> findByOwnerId(ObjectId ownerId);
    Stream<Group> findByName(String name);
    Stream<Group> findByNameLike(String name);
}
