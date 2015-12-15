package com.eigenmusik.services.music.soundcloud.domain;

/**
 * Created by timcoulson on 15/12/2015.
 */
import com.eigenmusik.services.music.soundcloud.Soundcloud;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SoundcloudTrack {

    private Long id;
    private String title;
    private SoundcloudUser user;

    public SoundcloudTrack() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SoundcloudUser getUser() {
        return user;
    }

    public void setUser(SoundcloudUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", user='" + user +
                '}';
    }
}
