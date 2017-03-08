package com.funcxy.oj.errors;


/**
 * Created by aak12 on 2017/3/7.
 */
public class NotFoundError extends Error {
    NotFoundError(String message) {
        super(404, message);
    }

    public NotFoundError() {
        this("Resource not found");
    }
}
