package com.eigenmusik.services.sources.repository;

import com.eigenmusik.services.sources.entity.SourceAccount;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository()
@RepositoryRestResource(exported = false)
public interface SourceAccountRepository extends PagingAndSortingRepository<SourceAccount, Long> {
}
