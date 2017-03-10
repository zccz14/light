package com.funcxy.oj.models;

import java.io.Serializable;

/**
 * @author aak1247 on 2017/3/7.
 */
public class CleanedProblem implements Serializable {
    public String id;
    public String title;

    public CleanedProblem(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
