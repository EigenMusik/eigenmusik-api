package com.eigenmusik.account;

import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UsernameExistsException;
import com.eigenmusik.user.UserProfile;
import com.eigenmusik.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountService {

    private UserProfileRepository userProfileRepository;
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, UserProfileRepository userProfileRepository) {
        this.accountRepository = accountRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public Account register(Account account) throws UsernameExistsException, EmailExistsException {

        // Check if a user with conflicting parameters exists.
        if (accountRepository.findByName(account.getName()) != null) {
            throw new UsernameExistsException();
        } else if (accountRepository.findByEmail(account.getEmail()) != null) {
            throw new EmailExistsException();
        }

        UserProfile profile = new UserProfile(account);
        profile.setDisplayName(account.getName());

        account.setActive(true);

        accountRepository.save(account);
        userProfileRepository.save(profile);

        return account;
    }
}
