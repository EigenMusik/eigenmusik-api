package com.eigenmusik.domain;

import com.eigenmusik.services.sources.soundcloud.SoundcloudAccessToken;

import javax.persistence.*;

@Entity
public class MusicSource {

    @Id
    @GeneratedValue
    private Long id;
    private String type;
    private String username;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private SoundcloudAccessToken accessToken;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private UserProfile createdBy;

    public MusicSource() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
