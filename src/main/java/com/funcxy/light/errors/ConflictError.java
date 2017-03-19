package com.funcxy.light.errors;

/**
 * Conflict Error: 409
 *
 * @author zccz14
 */
class ConflictError extends Error {
    ConflictError(String message) {
        super(409, message);
    }

    ConflictError() {
        this("Conflict");
    }
}
