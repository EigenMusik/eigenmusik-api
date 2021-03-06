package com.eigenmusik.api.sources.soundcloud;

import com.eigenmusik.api.sources.soundcloud.json.SoundcloudAccessTokenJson;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// TODO DRY the authtoken stuff up.
@Entity
public class SoundcloudAccessToken {

    @Id
    @GeneratedValue
    private Long id;
    private String accessToken;
    private String expiresIn;
    private String scope;
    private String refreshToken;

    public SoundcloudAccessToken() {

    }

    public SoundcloudAccessToken(SoundcloudAccessTokenJson soundcloudAccessTokenJson) {
        this.accessToken = soundcloudAccessTokenJson.getAccessToken();
        this.expiresIn = soundcloudAccessTokenJson.getExpiresIn();
        this.scope = soundcloudAccessTokenJson.getScope();
        this.refreshToken = soundcloudAccessTokenJson.getRefreshToken();
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

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}