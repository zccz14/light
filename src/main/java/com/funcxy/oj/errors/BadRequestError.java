package com.funcxy.oj.errors;

/**
 * 400 Bad Request
 *
 * @author zccz14
 */
public class BadRequestError extends Error {
    BadRequestError(String message) {
        super(400, message);
    }

    public BadRequestError() {
        this("Bad Request");
    }
}
