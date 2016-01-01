package com.eigenmusik.services.sources;

/**
 * Created by timcoulson on 31/12/2015.
 */

import com.eigenmusik.domain.StreamUrl;
import com.eigenmusik.domain.Track;
import com.eigenmusik.services.sources.soundcloud.SoundcloudGateway;
import com.eigenmusik.services.sources.soundcloud.SoundcloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by timcoulson on 31/12/2015.
 */
@Component
public class StreamService {

    @Autowired
    SourceServiceFactory sourceServiceFactory;

    public StreamUrl getStream(Track track) {
        SourceService sourceService = sourceServiceFactory.build(track.getSource());

        return sourceService.getStreamUrl(track);
    }
}
