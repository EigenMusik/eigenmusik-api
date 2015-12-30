package com.eigenmusik.exceptions;

/**
 * Created by timcoulson on 30/12/2015.
 */
public class UserDoesntExistException extends Throwable {
    @Override
    public String getMessage() {
        return "User doesn't exist.";
    }
}
