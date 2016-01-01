package com.eigenmusik.services.repository;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.UserProfile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository()
@RepositoryRestResource(exported = false)
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    UserProfile findByAccount(Account account);

}
