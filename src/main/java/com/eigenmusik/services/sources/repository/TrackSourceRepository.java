package com.eigenmusik.services.sources.repository;

import com.eigenmusik.services.sources.TrackSource;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository()
@RepositoryRestResource(exported = false)
public interface TrackSourceRepository extends PagingAndSortingRepository<TrackSource, Long> {
}
