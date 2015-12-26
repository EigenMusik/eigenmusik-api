package com.eigenmusik.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Track {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String uri;
    private String type;
    private Date createdOn;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private UserProfile createdBy;
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Artist artist;
    @ManyToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Album album;
    private Long durationMs;

    public Track() {

    }

    public Track(String name, Artist artist, Album album, String uri, String type, Long durationMs) {
        this.name = name;
        this.artist = artist;
        this.uri = uri;
        this.type = type;
        this.album = album;
        this.durationMs = durationMs;
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

    public Artist getArtist() {
        return artist;
    }

    public UserProfile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfile createdBy) {
        this.createdBy = createdBy;
    }

    public Album getAlbum() {
        return album;
    }

    public String getUri() {
        return uri;
    }

    public Long getDuration() {
        return durationMs;
    }

    public String getType() {
        return type;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

}
