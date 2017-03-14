package com.funcxy.oj.repositories;

import com.funcxy.oj.models.Oauth;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author  aak1247.
 */
public interface OauthRepository extends MongoRepository<Oauth,String>{

    Oauth findBySubmissionId(String submissionId);

}
