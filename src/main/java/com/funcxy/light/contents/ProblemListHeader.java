package com.funcxy.light.contents;

import com.funcxy.light.models.ProblemList;
import com.funcxy.light.models.ProblemListType;

import java.io.Serializable;

/**
 * 题单头部结构
 *
 * @author aak1247
 */
public class ProblemListHeader implements Serializable, ResponseContent {
    public String id;
    public String title;
    public ProblemListType type;

    public ProblemListHeader(String id, String title, ProblemListType type) {
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
