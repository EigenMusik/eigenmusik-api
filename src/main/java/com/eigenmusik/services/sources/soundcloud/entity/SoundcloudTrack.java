package com.eigenmusik.services.sources.soundcloud.entity;

/**
 * Created by timcoulson on 15/12/2015.
 */

import com.eigenmusik.domain.Track;
import com.eigenmusik.services.sources.soundcloud.json.SoundcloudTrackJson;

import javax.persistence.*;

@Entity
public class SoundcloudTrack {

    @Id
    @GeneratedValue
    private Long id;
    @Transient
    private String streamUrl;
    @Transient
    private String title;
    @Transient
    private String artist;
    private Long soundcloudId;
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Track track;
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SoundcloudUser owner;

    public SoundcloudTrack() {

    }

    public SoundcloudTrack(SoundcloudTrackJson response, SoundcloudUser owner) {
        this.streamUrl = response.getStreamUrl();
        this.title = response.getTitle();
        this.soundcloudId = response.getId();
        this.artist = response.getUser().getUsername();
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getSoundcloudId() {
        return soundcloudId;
    }

    public void setSoundcloudId(Long soundcloudId) {
        this.soundcloudId = soundcloudId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(SoundcloudUser user) {
        this.artist = artist;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public SoundcloudUser getOwner() {
        return owner;
    }

    public void setOwner(SoundcloudUser owner) {
        this.owner = owner;
    }
}
