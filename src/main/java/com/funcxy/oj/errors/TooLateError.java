package com.funcxy.oj.errors;

/**
 * Created by niyou2016 on 2017/3/13 0013.
 */
public class TooLateError extends Error {
    TooLateError(String message) {
        super(403, message);
    }

    public TooLateError() {
        this("Hand in too late");
    }
}

