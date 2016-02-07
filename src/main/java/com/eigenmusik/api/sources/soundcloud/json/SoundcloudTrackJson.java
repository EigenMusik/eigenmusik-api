package com.eigenmusik.api.sources.soundcloud.json;

/**
 * Created by timcoulson on 15/12/2015.
 */

import com.eigenmusik.api.sources.soundcloud.SoundcloudUser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SoundcloudTrackJson {

    private Long id;
    private String title;
    private SoundcloudUser user;
    @JsonProperty(value = "stream_url")
    private String streamUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SoundcloudUser getUser() {
        return user;
    }

    public void setUser(SoundcloudUser user) {
        this.user = user;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.streamUrl = streamUrl;
    }

}
