package com.eigenmusik.exceptions;

import com.eigenmusik.sources.SourceType;

public class SourceAuthenticationException extends Exception {

    private SourceType source;

    public void setSource(SourceType source) {
        this.source = source;
    }

    @Override
    public String getMessage() {
        return "Could not authenticate with " + source + ".";
    }

}
