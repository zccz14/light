package com.funcxy.light.repositories;

import com.funcxy.light.models.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.stream.Stream;

/**
 * @author aak1247.
 */
public interface GroupRepository extends MongoRepository<Group, String> {
    Group findById(String id);

    Group findOneByGroupName(String name);

    Stream<Group> findByOwnerId(String ownerId);

    Stream<Group> findByGroupName(String name);

    Stream<Group> findByGroupNameLike(String name);

    @Query("{ 'groupName' : { 'regex':?0}, 'type': {'regex':?1}}")
    Page<Group> roughFind(String groupName, String type, Pageable pageable);
}
