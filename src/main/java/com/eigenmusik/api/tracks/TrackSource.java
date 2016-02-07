package com.eigenmusik.api.tracks;

import com.eigenmusik.api.sources.SourceAccount;
import com.eigenmusik.api.sources.SourceType;

import javax.persistence.*;

@Entity
public class TrackSource {
    @Id
    @GeneratedValue
    private Long id;
    private String uri;
    private SourceType source;
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SourceAccount sourceAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public SourceType getSource() {
        return source;
    }

    public void setSource(SourceType source) {
        this.source = source;
    }

    public SourceAccount getOwner() {
        return sourceAccount;
    }

    public void setOwner(SourceAccount sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
}