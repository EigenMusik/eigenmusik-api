package com.eigenmusik.services;

import com.eigenmusik.domain.Account;
import com.eigenmusik.domain.UserProfile;
import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UsernameExistsException;
import com.eigenmusik.exceptions.UserDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Created by timcoulson on 24/12/2015.
 */
@Component
public class UserService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AccountRepository accountRepository;

    public void setUserProfileRepository(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public UserProfile getUserProfile(String username) throws UserDoesntExistException {
        Account account = accountRepository.findByName(username);
        if (account == null) {
            throw new UserDoesntExistException();
        }
        return getUserProfileByAccount(account);
    }

    private UserProfile getUserProfileByAccount(Account account) throws UserDoesntExistException {
        UserProfile userProfile = userProfileRepository.findByAccount(account);
        if (userProfile == null) {
            throw new UserDoesntExistException();
        }
        return userProfile;
    }

    public UserProfile getUserProfileByEmail(String email) throws UserDoesntExistException {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new UserDoesntExistException();
        }
        return getUserProfileByAccount(account);
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
        profile.setReputation(0);

        accountRepository.save(account);
        userProfileRepository.save(profile);

        return account;
    }
}
