package com.funcxy.light.repositories;

import com.funcxy.light.models.Oauth;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author aak1247.
 */
public interface OauthRepository extends MongoRepository<Oauth, String> {

    Oauth findBySubmissionId(String submissionId);

}
