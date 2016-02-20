package com.eigenmusik.api.sources.dropbox;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DropboxAccessTokenRepository extends CrudRepository<DropboxAccessToken, Long> {
}
