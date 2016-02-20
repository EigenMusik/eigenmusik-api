package com.eigenmusik.api.exceptions;

public class UserDoesntExistException extends Throwable {
    @Override
    public String getMessage() {
        return "USER_DOESNT_EXIST";
    }
}
