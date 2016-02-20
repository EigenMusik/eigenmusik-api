package com.eigenmusik.api.exceptions;

public class EmailExistsException extends Exception {

    @Override
    public String getMessage() {
        return "EMAIL_EXISTS";
    }
}
