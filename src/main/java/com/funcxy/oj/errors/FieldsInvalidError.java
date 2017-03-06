package com.funcxy.oj.errors;

import java.util.ArrayList;
import java.util.List;

/**
 * Fields Invalid
 *
 * @author zccz14
 */
public class FieldsInvalidError extends BadRequestError {
    private List<String> fields = new ArrayList<>();

    public FieldsInvalidError() {
        super("Fields Invalid");
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
