package com.eigenmusik.sources;

import com.eigenmusik.user.UserProfile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceAccountRepository extends PagingAndSortingRepository<SourceAccount, Long> {
    List<SourceAccount> findByOwner(UserProfile userProfile);
}
