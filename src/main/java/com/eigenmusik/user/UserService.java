package com.eigenmusik.user;

import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.exceptions.UsernameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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

    public User register(User user) throws UsernameExistsException, EmailExistsException {

        // Check if a user with conflicting parameters exists.
        if (userRepository.findByName(user.getName()) != null) {
            throw new UsernameExistsException();
        } else if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EmailExistsException();
        }

        user.setActive(true);
        UserProfile userProfile = new UserProfile();
        user.setUserProfile(userProfile);

        userProfileRepository.save(userProfile);
        userRepository.save(user);

        return user;
    }

    public User getByUsername(String username) throws UserDoesntExistException {
        User user = userRepository.findByName(username);
        if (user == null) {
            throw new UserDoesntExistException();
        }
        return user;
    }

    public User getByEmail(String email) throws UserDoesntExistException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserDoesntExistException();
        }
        return user;
    }
}
