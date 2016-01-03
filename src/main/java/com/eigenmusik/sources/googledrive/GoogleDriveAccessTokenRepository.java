package com.eigenmusik.sources.googledrive;

import com.eigenmusik.sources.soundcloud.SoundcloudAccessToken;
import com.google.api.client.auth.oauth2.Credential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Repository
public interface GoogleDriveAccessTokenRepository extends CrudRepository<GoogleDriveAccessToken, Long> {
}
