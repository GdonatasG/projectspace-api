package com.projectspace.projectspaceapi.user.service;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.user.model.User;
import com.projectspace.projectspaceapi.user.repository.UserRepository;
import com.projectspace.projectspaceapi.user.request.CreateUserRequest;
import com.projectspace.projectspaceapi.user.request.UpdateUserRequest;
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

    private final AuthenticationUserHelper authenticationUserHelper;

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
        user.setFirstName(createUserRequest.getFirst_name());
        user.setLastName(createUserRequest.getLast_name());
        user.setOrganizationName(createUserRequest.getOrganization_name());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRole("USER");
        userRepository.save(user);
    }

    public void updateUser(UpdateUserRequest updateUserRequest) {
        User user = authenticationUserHelper.getCurrentUser();

        if (updateUserRequest.getFirst_name() != null) {
            user.setFirstName(updateUserRequest.getFirst_name());
        }

        if (updateUserRequest.getLast_name() != null) {
            user.setLastName(updateUserRequest.getLast_name());
        }

        if (updateUserRequest.getOrganization_name() != null){
            user.setOrganizationName(updateUserRequest.getOrganization_name());
        }

        userRepository.save(user);
    }
}
