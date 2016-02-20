package com.eigenmusik.api.exceptions;

import com.eigenmusik.api.sources.SourceType;

public class SourceAuthenticationException extends Exception {

    private SourceType source;

    public SourceAuthenticationException(SourceType source) {
        this.source = source;
    }

    @Override
    public String getMessage() {
        return "SOURCE_AUTHENTICATION_EXCEPTION";
    }
}
