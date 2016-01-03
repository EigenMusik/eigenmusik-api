package com.eigenmusik.tracks;

import com.eigenmusik.sources.Source;
import com.eigenmusik.sources.SourceAccount;

import javax.persistence.*;

@Entity
public class TrackSource {
    @Id
    @GeneratedValue
    private Long id;
    private String uri;
    private Source source;
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public SourceAccount getOwner() {
        return sourceAccount;
    }

    public void setOwner(SourceAccount sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
}