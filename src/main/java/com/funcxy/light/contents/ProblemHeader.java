package com.funcxy.light.contents;

import com.funcxy.light.models.Problem;

import java.io.Serializable;

/**
 * 题目头部结构
 *
 * @author aak1247 zccz14
 */
public class ProblemHeader implements Serializable, ResponseContent {
    private final String id;
    private final String title;

    public ProblemHeader(String id, String title) {
        this.id = id;
        this.title = title;
    }

    private ProblemHeader(Problem problem) {
        this(problem.getId(), problem.getTitle());
    }

    public static ProblemHeader of(Problem problem) {
        return new ProblemHeader(problem);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
