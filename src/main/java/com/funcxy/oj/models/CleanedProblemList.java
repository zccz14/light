package com.funcxy.oj.models;

import java.io.Serializable;

/**
 * @author aak1247 on 2017/3/7.
 */
public class CleanedProblemList implements Serializable {
    public String id;
    public String title;
    public String type;

    public CleanedProblemList(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }
}
