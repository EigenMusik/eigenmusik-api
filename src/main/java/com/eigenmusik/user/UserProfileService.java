package com.eigenmusik.user;

import com.eigenmusik.account.Account;
import com.eigenmusik.account.AccountRepository;
import com.eigenmusik.exceptions.UserDoesntExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProfileService {

    private UserProfileRepository userProfileRepository;
    private AccountRepository accountRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, AccountRepository accountRepository) {
        this.userProfileRepository = userProfileRepository;
        this.accountRepository = accountRepository;
    }

    // TODO should make user profile a child of Account with hibernate
    public UserProfile getUserProfile(String username) throws UserDoesntExistException {
        Account account = accountRepository.findByName(username);
        if (account == null) {
            throw new UserDoesntExistException();
        }
        return getUserProfileByAccount(account);
    }

    public UserProfile getUserProfileByEmail(String email) throws UserDoesntExistException {
        Account account = accountRepository.findByEmail(email);
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
}
