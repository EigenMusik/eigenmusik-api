package com.eigenmusik.sources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by timcoulson on 01/02/2016.
 */
public class SourceJson {

    private String authUrl;
    private String name;
    private SourceType type;

    public SourceJson(String name, String authUrl, SourceType sourceType) {
        this.name = name;
        this.authUrl = authUrl;
        this.type = sourceType;
    }

    public String getName() {
        return name;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public SourceType getType() {
        return type;
    }
}
