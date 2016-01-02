package com.eigenmusik.sources.soundcloud;

/**
 * Created by timcoulson on 15/12/2015.
 */

import com.eigenmusik.sources.soundcloud.json.SoundcloudTrackJson;
import com.eigenmusik.tracks.Track;

public class SoundcloudTrack {

    private Long id;
    private String streamUrl;
    private String title;
    private String artist;
    private Long soundcloudId;
    private Track track;
    private SoundcloudUser owner;

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
