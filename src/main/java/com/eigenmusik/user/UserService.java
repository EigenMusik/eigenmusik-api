package com.eigenmusik.user;

import com.eigenmusik.exceptions.EmailExistsException;
import com.eigenmusik.exceptions.UserDoesntExistException;
import com.eigenmusik.exceptions.UsernameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
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
