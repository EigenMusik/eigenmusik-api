package com.eigenmusik.exceptions;

/**
 * Created by timcoulson on 24/12/2015.
 */
public class UsernameExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Username already exists in our database.";
    }

}
