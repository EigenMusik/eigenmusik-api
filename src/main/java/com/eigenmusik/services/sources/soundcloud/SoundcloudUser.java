package com.eigenmusik.services.sources.soundcloud;

import com.eigenmusik.domain.UserProfile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class SoundcloudUser {

    @Id
    private Long id;
    private String username;
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private SoundcloudAccessToken accessToken;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private UserProfile createdBy;

    public SoundcloudUser() {
    }

    public SoundcloudUser(Long id, String username, SoundcloudAccessToken accessToken, UserProfile createdBy) {
        this.id = id;
        this.username = username;
        this.accessToken = accessToken;
        this.createdBy = createdBy;
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

    public UserProfile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfile createdBy) {
        this.createdBy = createdBy;
    }

    public SoundcloudAccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(SoundcloudAccessToken accessToken) {
        this.accessToken = accessToken;
    }
}
