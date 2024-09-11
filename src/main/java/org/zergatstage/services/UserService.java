package org.zergatstage.services;

import org.springframework.stereotype.Service;
import org.zergatstage.model.User;
import org.zergatstage.repository.UserRepository;

/**
 * @author father
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String username){
        return userRepository.save( User.builder()
                        .username(username)
                .build());
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
