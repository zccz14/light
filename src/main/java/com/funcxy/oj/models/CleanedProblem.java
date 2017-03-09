package com.funcxy.oj.models;

import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * @author aak1247 on 2017/3/7.
 */
public class CleanedProblem implements Serializable {
    public ObjectId id;
    public String title;
    public CleanedProblem(ObjectId id, String title) {
        this.id = id;
        this.title = title;
    }
}
