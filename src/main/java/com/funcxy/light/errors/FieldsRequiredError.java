package com.funcxy.light.errors;

import java.util.ArrayList;
import java.util.List;

/**
 * Fields Required: 400
 *
 * @author zccz14
 */
public class FieldsRequiredError extends BadRequestError {
    private List<String> fields = new ArrayList<>();

    public FieldsRequiredError() {
        super("Fields Required");
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}
