package com.eigenmusik.services.sources.soundcloud;

/**
 * Created by timcoulson on 15/12/2015.
 */

import com.eigenmusik.domain.Track;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.codehaus.jackson.map.ObjectMapper;

import javax.persistence.*;
import java.io.IOException;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoundcloudTrack {

    @Id
    private Long id;
    private String title;
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SoundcloudUser user;
    @JsonProperty(value = "stream_url")
    private String streamUrl;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Track track;

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

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
