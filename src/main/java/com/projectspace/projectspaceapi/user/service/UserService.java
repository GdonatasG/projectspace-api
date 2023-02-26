package com.projectspace.projectspaceapi.user.service;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.user.model.User;
import com.projectspace.projectspaceapi.user.repository.UserRepository;
import com.projectspace.projectspaceapi.user.request.CreateUserRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User readUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    public User readUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
    }

    public void createUser(CreateUserRequest createUserRequest) {
        Optional<User> byEmail = userRepository.findByEmail(createUserRequest.getEmail());
        if (byEmail.isPresent()) {
            throw new AlreadyTakenException("Email already taken!");
        }

        Optional<User> byUsername = userRepository.findByUsername(createUserRequest.getUsername());
        if (byUsername.isPresent()) {
            throw new AlreadyTakenException("Username already taken!");
        }

        User user = new User();

        user.setUsername(createUserRequest.getUsername());
        user.setFirstName(createUserRequest.getFirstName());
        user.setLastName(createUserRequest.getLastName());
        user.setOrganizationName(createUserRequest.getOrganizationName());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRole(createUserRequest.getRole());
        userRepository.save(user);
    }
}
