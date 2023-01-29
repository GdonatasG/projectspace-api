package com.projectspace.projectspaceapi.user.service;

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

    public void createUser(CreateUserRequest createUserRequest) {
        User user = new User();
        Optional<User> byEmail = userRepository.findByEmail(createUserRequest.getEmail());
        if (byEmail.isPresent()) {
            throw new RuntimeException("Email already taken!");
        }
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        userRepository.save(user);
    }
}
