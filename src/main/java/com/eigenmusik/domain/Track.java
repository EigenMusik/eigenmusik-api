package com.eigenmusik.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Track {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String artist;
    private String ref;
    private String type;
    private Date createdOn;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    private UserProfile createdBy;

    public Track() {

    }

    public Track(String name, String artist, String ref, String type) {
        this.name = name;
        this.artist = artist;
        this.ref = ref;
        this.type = type;
    }

    public UserProfile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserProfile createdBy) {
        this.createdBy = createdBy;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String name) {
        this.ref = ref;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

}
