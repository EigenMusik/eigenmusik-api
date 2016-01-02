package com.eigenmusik.sources;

import com.eigenmusik.sources.soundcloud.SoundcloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceServiceFactory {

    private final SoundcloudService soundcloudService;

    @Autowired
    public SourceServiceFactory(SoundcloudService soundcloudService) {
        this.soundcloudService = soundcloudService;
    }

    public SourceService build(Source source) {
        switch (source) {
            case SOUNDCLOUD:
                return soundcloudService;
            default:
                return null;
        }
    }
}
