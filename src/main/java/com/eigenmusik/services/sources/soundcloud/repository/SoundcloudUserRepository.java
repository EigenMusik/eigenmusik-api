package com.eigenmusik.services.sources.soundcloud.repository;

import com.eigenmusik.services.sources.soundcloud.entity.SoundcloudUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundcloudUserRepository extends CrudRepository<SoundcloudUser, Long> {
}
