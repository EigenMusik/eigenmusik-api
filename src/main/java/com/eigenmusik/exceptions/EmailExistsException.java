package com.eigenmusik.exceptions;

/**
 * Created by timcoulson on 24/12/2015.
 */
public class EmailExistsException extends Exception {

    @Override
    public String getMessage() {
        return "Email already exists in our database.";
    }
}
