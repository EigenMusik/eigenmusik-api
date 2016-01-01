package com.eigenmusik.services.sources;

import com.eigenmusik.services.sources.soundcloud.SoundcloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by timcoulson on 01/01/2016.
 */
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
