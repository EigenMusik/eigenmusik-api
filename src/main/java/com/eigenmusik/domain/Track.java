package com.eigenmusik.domain;

import com.eigenmusik.services.sources.Source;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Track {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String uri;
    private Source source;
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

    public Track(String name, Artist artist, Album album, String uri, Source source, Long durationMs) {
        this.name = name;
        this.artist = artist;
        this.uri = uri;
        this.source = source;
        this.album = album;
        this.durationMs = durationMs;
    }

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

    public Source getSource() {
        return source;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

}
