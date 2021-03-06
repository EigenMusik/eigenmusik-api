package com.eigenmusik.api.tracks;

import com.eigenmusik.api.user.UserProfile;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Track {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String artist;

    // TODO populate these with real data.
    private Long duration = 12345L;
    private String album = "An album";
    private Date createdOn = new Date();

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    private TrackSource trackSource;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private UserProfile createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfile createdBy) {
        this.createdBy = createdBy;
    }

    public TrackSource getTrackSource() {
        return trackSource;
    }

    public void setTrackSource(TrackSource trackSource) {
        this.trackSource = trackSource;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
