package com.eigenmusik.services.sources.soundcloud.repository;

import com.eigenmusik.domain.Track;
import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudTrack;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by timcoulson on 31/12/2015.
 */
@Repository
public interface SoundcloudTrackRepository extends CrudRepository<SoundcloudTrack, Long> {
    SoundcloudTrack findByTrack(Track track);
}
