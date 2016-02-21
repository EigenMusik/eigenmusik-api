package com.eigenmusik.api.common;

import java.util.Map;

public class ErrorJson {
    private String message;
    private Map<String, String> params;

    public ErrorJson(String message, Map<String, String> params) {
        this.message = message;
        this.params = params;
    }

    ;

    public String getMessage() {
        return message;
    }

    public Map<String, String> getParams() {
        return params;
    }
}