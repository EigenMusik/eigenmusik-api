package com.eigenmusik.api.sources.googledrive;

import com.google.api.client.auth.oauth2.Credential;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class GoogleDriveAccessToken {

    @Id
    @GeneratedValue
    private Long id;
    private String accessToken;
    private String expiresIn;
    private String refreshToken;

    public GoogleDriveAccessToken() {

    }

    public GoogleDriveAccessToken(Credential credential) {
        this.accessToken = credential.getAccessToken();
        this.expiresIn = credential.getExpiresInSeconds().toString();
        this.refreshToken = credential.getRefreshToken();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
