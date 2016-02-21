package com.eigenmusik.api.common;

public class ValidationException extends Throwable {

    private Errors errors;

    public ValidationException(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
