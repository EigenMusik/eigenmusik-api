package com.eigenmusik.api.sources;

import java.util.HashMap;
import java.util.Map;

/**
 * An object to map external source authentication requests.
 */
public class SourceAccountAuthentication {

    private String code;
    private String state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<String, String[]> toParameterMap() {
        Map<String, String[]> params = new HashMap<>();
        params.put("code", new String[]{code});
        params.put("state", new String[]{state});

        return params;
    }
}