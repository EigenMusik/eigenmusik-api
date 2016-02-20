package com.eigenmusik.api.sources.soundcloud;

import com.eigenmusik.api.sources.soundcloud.json.SoundcloudUserJson;

import javax.persistence.*;

@Entity
// TODO can we have a user and access token interface, or some kind of Oauth interface?
public class SoundcloudUser {

    @Id
    @GeneratedValue
    private Long id;
    private Long soundcloudId;
    private String username;
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private SoundcloudAccessToken accessToken;

    public SoundcloudUser() {
    }

    public SoundcloudUser(SoundcloudUserJson soundcloudUserJson) {
        this.username = soundcloudUserJson.getUsername();
        this.soundcloudId = soundcloudUserJson.getId();

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

    public SoundcloudAccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(SoundcloudAccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public Long getSoundcloudId() {
        return soundcloudId;
    }

    public void setSoundcloudId(Long soundcloudId) {
        this.soundcloudId = soundcloudId;
    }
}
