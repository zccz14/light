package com.funcxy.oj.errors;


/**
 * @author aak1247 on 2017/3/7.
 */
public class NotFoundError extends Error {
    private NotFoundError(String message) {
        super(404, message);
    }

    public NotFoundError() {
        this("Resource not found");
    }
}
