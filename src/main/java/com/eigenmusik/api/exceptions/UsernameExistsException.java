package com.eigenmusik.api.exceptions;

public class UsernameExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Username already exists in our database.";
    }

}
