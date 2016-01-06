package com.eigenmusik.sources.googledrive;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoogleDriveUserRepository extends CrudRepository<GoogleDriveUser, Long> {
}
