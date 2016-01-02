package com.eigenmusik.tracks;

import com.eigenmusik.user.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository()
@RepositoryRestResource(exported = false)
public interface TrackRepository extends PagingAndSortingRepository<Track, Long> {
    Page<Track> createdBy(@Param("name") UserProfile userProfile, Pageable pageable);
}
