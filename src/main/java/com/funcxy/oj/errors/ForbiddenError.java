package com.funcxy.oj.errors;

/**
 * Created by wtupc96 on 2017/3/7.
 *
 * @author Peter
 * @version 1.0
 */
public class ForbiddenError extends Error {
    ForbiddenError(String message){
        super(403, message);
    }

    public ForbiddenError() {
        this("Access forbidden.");
    }
}
