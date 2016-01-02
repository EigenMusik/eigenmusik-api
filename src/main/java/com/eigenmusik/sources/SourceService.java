package com.eigenmusik.sources;

import com.eigenmusik.tracks.TrackStreamUrl;
import com.eigenmusik.tracks.Track;

import java.util.List;

public interface SourceService {

    TrackStreamUrl getStreamUrl(Track track);

    SourceAccount getAccount(String authCode);

    List<Track> getTracks(SourceAccount account);
}
