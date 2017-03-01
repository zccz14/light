package com.funcxy.oj.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by Administrator on 2017/2/28 0028.
 */
public class Submission {
    @Id
    private ObjectId id;
    @Indexed
    private ObjectId problemlistid;
    @Indexed
    private ObjectId problemid;
    @Indexed
    private String status;
    private String condition;
    private String sentence;
    private String content;

    public ObjectId getProblemlistid() {
        return problemlistid;
    }

    public void setProblemlistid(ObjectId problemlistid) {
        this.problemlistid = problemlistid;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getProblemid() {
        return problemid;
    }

    public void setProblemid(ObjectId problemid) {
        this.problemid = problemid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
