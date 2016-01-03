package com.eigenmusik.sources;

import com.eigenmusik.sources.googledrive.GoogleDriveService;
import com.eigenmusik.sources.soundcloud.SoundcloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceServiceFactory {

    private final SoundcloudService soundcloudService;
    private final GoogleDriveService googleDriveService;

    @Autowired
    public SourceServiceFactory(
            SoundcloudService soundcloudService,
            GoogleDriveService googleDriveService
                                ) {
        this.soundcloudService = soundcloudService;
        this.googleDriveService = googleDriveService;
    }

    public SourceService build(Source source) {
        switch (source) {
            case SOUNDCLOUD:
                return soundcloudService;
            case GOOGLEDRIVE:
                return googleDriveService;
            default:
                return null;
        }
    }
}
