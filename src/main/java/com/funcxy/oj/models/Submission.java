package com.funcxy.oj.models;

import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Created by niyou2016 on 2017/2/28 0028.
 */
public class Submission extends Model {
    /**
     * 提交者 ID
     */
    @Indexed
    private String userId;
    /**
     * 题单 ID
     */
    @Indexed
    private String problemListId;
    /**
     * 题目 ID
     */
    @Indexed
    private String problemId;
    /**
     * 提交状态
     */
    private String status; // TODO: use enum
    /**
     * 判决结果
     */
    private String sentence;
    /**
     * 提交者提交的内容
     */
    private String content;

    public String getProblemListId() {
        return problemListId;
    }

    public void setProblemListId(String problemListId) {
        this.problemListId = problemListId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
