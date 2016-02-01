package com.eigenmusik.sources;

import com.eigenmusik.user.UserProfile;

import javax.persistence.*;

@Entity
public class SourceAccount {

    @Id
    @GeneratedValue
    private Long id;
    private Long uri;
    private SourceType source;
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserProfile owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUri() {
        return uri;
    }

    public void setUri(Long uri) {
        this.uri = uri;
    }

    public SourceType getSource() {
        return source;
    }

    public void setSource(SourceType source) {
        this.source = source;
    }

    public UserProfile getOwner() {
        return owner;
    }

    public void setOwner(UserProfile owner) {
        this.owner = owner;
    }
}