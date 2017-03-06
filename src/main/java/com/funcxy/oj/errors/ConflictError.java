package com.funcxy.oj.errors;

/**
 * Conflict Error: 409
 *
 * @author zccz14
 */
public class ConflictError extends Error {
    ConflictError(String message) {
        super(409, message);
    }

    public ConflictError() {
        this("Conflict");
    }
}
