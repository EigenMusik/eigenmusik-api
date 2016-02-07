package com.eigenmusik.api.sources.soundcloud;

/**
 * Created by timcoulson on 26/12/2015.
 */

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundcloudAccessTokenRepository extends CrudRepository<SoundcloudAccessToken, Long> {
}
