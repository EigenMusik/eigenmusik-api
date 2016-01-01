package com.eigenmusik.services.sources;

import com.eigenmusik.domain.StreamUrl;
import com.eigenmusik.domain.Track;

/**
 * Created by timcoulson on 01/01/2016.
 */
public interface SourceService {

    StreamUrl getStreamUrl(Track track);
}
