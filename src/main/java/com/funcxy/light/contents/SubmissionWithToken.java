package com.funcxy.light.contents;

import com.funcxy.light.models.Submission;

/**
 * @author aak1247.
 */
public class SubmissionWithToken extends Submission {
    public String token;

    public Object refAnswer;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getRefAnswer(){
        return this.refAnswer;
    }

    public void setRefAnswer(Object refAnswer) {
        this.refAnswer = refAnswer;
    }
}
