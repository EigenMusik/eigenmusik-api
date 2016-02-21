package com.eigenmusik.api.user;

import com.eigenmusik.api.common.Errors;
import com.eigenmusik.api.common.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not not found");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT"));
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    /**
     * Register the given user.
     *
     * @param user
     * @return
     * @throws ValidationException
     */
    public User register(User user) throws ValidationException {

        // Validate user.
        Errors errors = new Errors();
        if (userRepository.findByName(user.getName()) != null) {
            errors.addError("USERNAME_EXISTS");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            errors.addError("EMAIL_EXISTS");
        }
        if (errors.notEmpty()) {
            throw new ValidationException(errors);
        }

        user.setActive(true);
        UserProfile userProfile = new UserProfile();
        user.setUserProfile(userProfile);

        userProfileRepository.save(userProfile);
        userRepository.save(user);

        return user;
    }

    /**
     * Get a user from a given username.
     *
     * @param username
     * @return
     * @throws UserDoesntExistException
     */
    public User getByUsername(String username) throws UserDoesntExistException {
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new UserDoesntExistException();
        }
        return user;
    }

    /**
     * Get a user from a given email address.
     *
     * @param email
     * @return
     * @throws UserDoesntExistException
     */
    public User getByEmail(String email) throws UserDoesntExistException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserDoesntExistException();
        }
        return user;
    }
}
