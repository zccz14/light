package com.funcxy.light.errors;

/**
 * Created by wtupc96 on 2017/3/10.
 *
 * @author Peter
 * @version 1.0
 */
public class UnsupportedMediaType extends Error {
    private UnsupportedMediaType(String message) {
        super(415, message);
    }

    public UnsupportedMediaType() {
        this("Unsupported Media Type.");
    }
}
