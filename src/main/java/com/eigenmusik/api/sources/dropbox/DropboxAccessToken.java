package com.eigenmusik.api.sources.dropbox;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DropboxAccessToken {

    @Id
    @GeneratedValue
    private Long id;
    private String accessToken;

    public DropboxAccessToken() {
    }

    public DropboxAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

