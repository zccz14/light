package com.funcxy.oj.models;

import org.bson.types.ObjectId;

import java.io.Serializable;

/**
 * Created by aak12 on 2017/3/7.
 */
public class CleanedProblemList implements Serializable {
    public ObjectId id;
    public String title;
    public String type;

    public CleanedProblemList(ObjectId id, String title,String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }
}
