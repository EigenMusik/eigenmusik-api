package com.eigenmusik.sources;

import com.eigenmusik.sources.dropbox.Dropbox;
import com.eigenmusik.sources.googledrive.GoogleDrive;
import com.eigenmusik.sources.soundcloud.Soundcloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SourceFactory {

    private final Soundcloud soundcloudService;
    private final GoogleDrive googleDriveService;
    private final Dropbox dropboxService;

    @Autowired
    public SourceFactory(
            Soundcloud soundcloudService,
            GoogleDrive googleDriveService,
            Dropbox dropboxService
                                ) {
        this.soundcloudService = soundcloudService;
        this.googleDriveService = googleDriveService;
        this.dropboxService = dropboxService;
    }

    public Source build(SourceType source) {
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
