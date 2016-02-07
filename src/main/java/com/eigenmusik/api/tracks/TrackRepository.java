package com.eigenmusik.api.tracks;

import com.eigenmusik.api.user.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends PagingAndSortingRepository<Track, Long> {
    Page<Track> createdBy(@Param("name") UserProfile userProfile, Pageable pageable);
}
