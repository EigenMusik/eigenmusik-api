package com.eigenmusik.services;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UsernameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by timcoulson on 24/12/2015.
 */
@Component
public class UserService {

    @Autowired
    private UserProfileRepository userProfileRepository;


    @Autowired
    private AccountRepository accountRepository;

    public UserProfile getUserProfile(String username) {
       return userProfileRepository.findByAccount(accountRepository.findByName(username));
    }

    public UserProfile getUserProfileByEmail(String email) {
        return userProfileRepository.findByAccount(accountRepository.findByEmail(email));
    }

    public Account register(Account account) throws UsernameExistsException, EmailExistsException {

        // Check if a user with conflicting parameters exists.
        if (getUserProfile(account.getName()) != null) {
            throw new UsernameExistsException();
        } else if (getUserProfileByEmail(account.getEmail()) != null) {
            throw new EmailExistsException();
        }

        UserProfile profile = new UserProfile(account);
        profile.setDisplayName(account.getName());
        profile.setReputation(0);

        accountRepository.save(account);
        userProfileRepository.save(profile);

        return account;
    }
}
