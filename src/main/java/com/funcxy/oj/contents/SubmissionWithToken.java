package com.funcxy.oj.contents;

import com.funcxy.oj.models.Submission;

/**
 * @author aak1247.
 */
public class SubmissionWithToken extends Submission {
    public String token;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
