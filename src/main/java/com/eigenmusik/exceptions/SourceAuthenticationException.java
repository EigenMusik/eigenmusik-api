package com.eigenmusik.exceptions;

import com.eigenmusik.sources.Source;

public class SourceAuthenticationException extends Exception {

    private Source source;

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public String getMessage() {
        return "Could not authenticate with " + source + ".";
    }

}
