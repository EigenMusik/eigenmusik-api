package com.eigenmusik.tracks;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackSourceRepository extends PagingAndSortingRepository<TrackSource, Long> {
}
