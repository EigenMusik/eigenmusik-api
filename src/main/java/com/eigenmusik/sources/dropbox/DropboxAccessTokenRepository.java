package com.eigenmusik.sources.dropbox;

import com.eigenmusik.sources.soundcloud.SoundcloudAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DropboxAccessTokenRepository  extends CrudRepository<DropboxAccessToken, Long> {

}
