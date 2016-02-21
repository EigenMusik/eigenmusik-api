package com.eigenmusik.api.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Errors {
    private List<ErrorJson> errors = new ArrayList<>();

    public Errors() {
    }

    ;

    public static Errors newError(String message, Map<String, String> params) {
        Errors errors = new Errors();
        errors.addError(message, params);
        return errors;
    }

    public static Errors newError(String message) {
        Errors errors = new Errors();
        errors.addError(message);
        return errors;
    }

    public List<ErrorJson> getErrors() {
        return errors;
    }

    public void addError(String message) {
        this.errors.add(new ErrorJson(message, new HashMap<String, String>()));
    }

    public void addError(String message, Map<String, String> params) {
        this.errors.add(new ErrorJson(message, params));
    }

    public Boolean notEmpty() {
        return !errors.isEmpty();
    }
}
