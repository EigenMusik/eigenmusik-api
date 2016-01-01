package com.eigenmusik.services.sources.soundcloud.repository;

/**
 * Created by timcoulson on 26/12/2015.
 */

import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundcloudAccessTokenRepository extends CrudRepository<SoundcloudAccessToken, Long> {
}
