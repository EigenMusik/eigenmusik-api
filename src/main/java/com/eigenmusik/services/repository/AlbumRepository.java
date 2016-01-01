package com.eigenmusik.services.repository;

import com.eigenmusik.domain.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository()
@RepositoryRestResource(exported = false)
public interface AlbumRepository extends PagingAndSortingRepository<Album, Long> {
    Page<Album> findByNameContains(@Param("name") String name, Pageable pageable);
}
