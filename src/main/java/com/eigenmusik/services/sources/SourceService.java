package com.eigenmusik.services.sources;

import com.eigenmusik.domain.StreamUrl;
import com.eigenmusik.domain.Track;
import com.eigenmusik.services.sources.entity.SourceAccount;

import java.util.List;

/**
 * Created by timcoulson on 01/01/2016.
 */
public interface SourceService {

    StreamUrl getStreamUrl(Track track);

    SourceAccount getAccount(String authCode);

    List<Track> getTracks(SourceAccount account);
}
