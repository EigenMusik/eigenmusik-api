package com.eigenmusik.sources.dropbox;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class DropboxAccessToken  {

    @Id
    @GeneratedValue
    private Long id;
    private String accessToken;

    public DropboxAccessToken() {

    }

    public DropboxAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}

