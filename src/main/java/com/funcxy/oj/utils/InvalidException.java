package com.funcxy.oj.utils;

/**
 * Created by aak1247 on 2017/3/1.
 */
public class InvalidException extends Throwable {
    String message = "model invalid";
    public InvalidException(){}
    public InvalidException(String message){
        this.message=message;
    }
}
