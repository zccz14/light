package com.funcxy.oj.models;

/**
 * The base class for all models
 *
 * @author zccz14
 */
public abstract class Model {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
