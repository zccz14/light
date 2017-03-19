package com.funcxy.light.models;

/**
 * The base class for all models
 *
 * @author zccz14
 */
public abstract class Model {
    /**
     * ID
     */
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
