package com.eigenmusik.services.sources.soundcloud.entity;

import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudAccessToken;
import com.eigenmusik.services.sources.soundcloud.json.SoundcloudUserJson;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
public class SoundcloudUser {

    @Id
    @GeneratedValue
    private Long id;
    private Long soundcloudId;
    private String username;
    @OneToOne(optional = true, fetch = FetchType.LAZY)
    private SoundcloudAccessToken accessToken;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private UserProfile createdBy;

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

    public UserProfile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfile createdBy) {
        this.createdBy = createdBy;
    }


    public Long getSoundcloudId() {
        return soundcloudId;
    }

    public void setSoundcloudId(Long soundcloudId) {
        this.soundcloudId = soundcloudId;
    }
}
