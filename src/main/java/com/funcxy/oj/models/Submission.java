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
    private ObjectId problemListId;
    @Indexed
    private ObjectId problemId;
    @Indexed
    private String status;
    private String condition;
    private String sentence;
    private String content;

    public ObjectId getProblemListId() {
        return problemListId;
    }

    public void setProblemListId(ObjectId problemListId) {
        this.problemListId = problemListId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public ObjectId getProblemId() {
        return problemId;
    }

    public void setProblemId(ObjectId problemId) {
        this.problemId = problemId;
    }
}
