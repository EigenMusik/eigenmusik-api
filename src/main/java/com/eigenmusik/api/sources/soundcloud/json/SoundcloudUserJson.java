package com.eigenmusik.api.sources.soundcloud.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * JSON mapping of the Soundcloud user response.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoundcloudUserJson {

    private Long id;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
