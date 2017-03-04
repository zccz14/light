package com.funcxy.oj.errors;

import java.util.ArrayList;
import java.util.List;

/**
 * Fields Duplicated: 409
 *
 * @author zccz14
 */
public class FieldsDuplicateError extends ConflictError {
    private List<String> fields = new ArrayList<>();

    public FieldsDuplicateError() {
        super("Fields Duplicated");
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
