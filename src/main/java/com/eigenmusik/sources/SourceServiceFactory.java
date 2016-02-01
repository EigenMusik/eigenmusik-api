package com.eigenmusik.sources;

import com.eigenmusik.sources.dropbox.DropboxService;
import com.eigenmusik.sources.googledrive.GoogleDriveService;
import com.eigenmusik.sources.soundcloud.SoundcloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceServiceFactory {

    private final SoundcloudService soundcloudService;
    private final GoogleDriveService googleDriveService;
    private final DropboxService dropboxService;

    @Autowired
    public SourceServiceFactory(
            SoundcloudService soundcloudService,
            GoogleDriveService googleDriveService,
            DropboxService dropboxService
                                ) {
        this.soundcloudService = soundcloudService;
        this.googleDriveService = googleDriveService;
        this.dropboxService = dropboxService;
    }

    public SourceService build(SourceType source) {
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
