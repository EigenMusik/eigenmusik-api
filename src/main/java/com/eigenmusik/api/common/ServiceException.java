package com.eigenmusik.api.common;

public abstract class ServiceException extends Throwable {
    public abstract ErrorJson getError();
}
