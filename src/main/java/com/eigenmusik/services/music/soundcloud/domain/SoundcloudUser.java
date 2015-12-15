package com.eigenmusik.services.music.soundcloud.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SoundcloudUser {

    private String username;

    public SoundcloudUser() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "SoundcloudUser{" + "username='" + username + "\'}";
    }
}
