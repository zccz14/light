package com.funcxy.light.models;

import org.hibernate.validator.constraints.URL;

/**
 * 代理
 *
 * @author aak1247 on 2017/3/10.
 */
public class Proxy {
    /**
     * 代理地址
     */
    @URL
    private String url;
    /**
     * 代理的问题类型(如编程题)
     */
    private ProblemType type;
    /**
     * 代理的提交类型(如C语言)
     */
    private String submissionType;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ProblemType getType() {
        return this.type;
    }

    public void setType(ProblemType type) {
        this.type = type;
    }

    public String getSubmissionType() {
        return this.submissionType;
    }

    public void setSubmissionType(String type) {
        this.submissionType = type;
    }
}
