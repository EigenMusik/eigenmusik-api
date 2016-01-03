package com.eigenmusik.sources.googledrive;

import com.eigenmusik.sources.soundcloud.SoundcloudAccessToken;
import com.eigenmusik.sources.soundcloud.json.SoundcloudUserJson;
import com.google.api.services.oauth2.model.Userinfoplus;

import javax.persistence.*;

@Entity
public class GoogleDriveUser {

    @Id
    @GeneratedValue
    private Long id;
    private String googleId;
    private String username;
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private GoogleDriveAccessToken accessToken;

    public GoogleDriveUser() {
    }

    public GoogleDriveUser(Userinfoplus userinfoplus) {
        this.googleId = userinfoplus.getId();
        this.username = userinfoplus.getName();
    }

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

    public GoogleDriveAccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(GoogleDriveAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}
