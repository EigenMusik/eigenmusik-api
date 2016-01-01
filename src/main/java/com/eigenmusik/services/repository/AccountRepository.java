package com.eigenmusik.services.repository;

import com.eigenmusik.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    Account findByName(String name);

    Account findByEmail(String email);
}
