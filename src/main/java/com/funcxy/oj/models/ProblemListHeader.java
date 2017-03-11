package com.funcxy.oj.models;

import java.io.Serializable;

/**
 * 题单头部结构
 *
 * @author aak1247
 */
public class ProblemListHeader implements Serializable, ResponseContent {
    public String id;
    public String title;
    public String type;

    public ProblemListHeader(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    private ProblemListHeader(ProblemList problemList) {
        this.id = problemList.getId();
        this.title = problemList.getTitle();
        this.type = problemList.getType();
    }

    public static ProblemListHeader of(ProblemList problemList) {
        return new ProblemListHeader(problemList);
    }
}
