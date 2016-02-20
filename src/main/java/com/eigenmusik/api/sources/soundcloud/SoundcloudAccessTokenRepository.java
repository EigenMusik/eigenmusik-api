package com.eigenmusik.api.sources.soundcloud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundcloudAccessTokenRepository extends CrudRepository<SoundcloudAccessToken, Long> {
}
