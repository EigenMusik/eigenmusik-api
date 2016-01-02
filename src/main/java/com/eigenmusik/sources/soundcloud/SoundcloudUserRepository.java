package com.eigenmusik.sources.soundcloud;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundcloudUserRepository extends CrudRepository<SoundcloudUser, Long> {
}
