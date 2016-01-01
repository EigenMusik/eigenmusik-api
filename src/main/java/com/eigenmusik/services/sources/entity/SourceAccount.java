package com.eigenmusik.services.sources.entity;

import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.services.sources.Source;

import javax.persistence.*;

/**
 * Created by timcoulson on 01/01/2016.
 */
@Entity
public class SourceAccount {

    @Id
    @GeneratedValue
    private Long id;
    private Long uri;
    private Source source;
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public UserProfile getOwner() {
        return owner;
    }

    public void setOwner(UserProfile owner) {
        this.owner = owner;
    }
}