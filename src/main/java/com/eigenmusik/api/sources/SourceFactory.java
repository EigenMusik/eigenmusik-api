package com.eigenmusik.api.sources;

import com.eigenmusik.api.sources.dropbox.Dropbox;
import com.eigenmusik.api.sources.googledrive.GoogleDrive;
import com.eigenmusik.api.sources.soundcloud.Soundcloud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Given a source type, resolve the Source.
 */
@Component
public class SourceFactory {

    private final Soundcloud soundcloud;
    private final GoogleDrive googleDrive;
    private final Dropbox dropbox;

    @Autowired
    public SourceFactory(
            Soundcloud soundcloud,
            GoogleDrive googleDrive,
            Dropbox dropbox
    ) {
        this.soundcloud = soundcloud;
        this.googleDrive = googleDrive;
        this.dropbox = dropbox;
    }

    public Source build(SourceType source) {
        switch (source) {
            case SOUNDCLOUD:
                return soundcloud;
            case GOOGLEDRIVE:
                return googleDrive;
            case DROPBOX:
                return dropbox;
            default:
                return null;
        }
    }
}
