package com.eigenmusik.services;

import com.eigenmusik.domain.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository()
@RepositoryRestResource(exported = false)
public interface ArtistRepository extends PagingAndSortingRepository<Artist, Long> {
    Page<Artist> findByNameContains(@Param("name") String name, Pageable pageable);
}
