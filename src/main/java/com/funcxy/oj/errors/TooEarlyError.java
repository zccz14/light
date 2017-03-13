package com.funcxy.oj.errors;

/**
 * Created by niyou1996 on 2017/3/10 0010.
 */
public class TooEarlyError extends Error {
    TooEarlyError(String message) {
        super(403, message);
    }

    public TooEarlyError() {
        this("Hand in too early");
    }
}

