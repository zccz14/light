package com.funcxy.light.models;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author aak1247
 */
@Document(collection = "Oauths")
public class Oauth extends Model {
    /**
     * Oauth 鉴权依据
     */
    private String token;
    /**
     * 对应题目ID
     */
    @Indexed(unique = true)
    private String submissionId;

    public void setToken(String token) {
        this.token = token;
    }

    //    public String getToken(){
//        return this.token;
//    }
    public boolean verifyToken(String token) {
        return this.token.equals(token);
    }

    public String getSubmissionId() {
        return this.submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }
}
