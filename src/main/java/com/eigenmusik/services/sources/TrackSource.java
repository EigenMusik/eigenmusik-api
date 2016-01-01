package com.eigenmusik.services.sources;

import com.eigenmusik.services.sources.entity.SourceAccount;

import javax.persistence.*;

/**
 * Created by timcoulson on 01/01/2016.
 */
@Entity
public class TrackSource {
    @Id
    @GeneratedValue
    private Long id;
    @Transient
    private String streamUrl;
    private Long uri;
    private Source source;
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SourceAccount sourceAccount;

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

    public SourceAccount getOwner() {
        return sourceAccount;
    }

    public void setOwner(SourceAccount sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
}