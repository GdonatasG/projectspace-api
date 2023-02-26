package com.projectspace.projectspaceapi.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.user.model.User;
import com.projectspace.projectspaceapi.user.model.VisualUser;
import com.projectspace.projectspaceapi.user.request.CreateUserRequest;
import com.projectspace.projectspaceapi.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.USER_URL)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        userService.createUser(createUserRequest);
        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @GetMapping("/test")
    // TODO: delete, just for testing purposes
    public ResponseEntity test() {
        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<VisualUser> getCurrentUser() {
        User user = AuthenticationUserHelper.getCurrentUser(userService);

        VisualUser visualUser = new VisualUser();
        visualUser.setUsername(user.getUsername());
        visualUser.setFirstName(user.getFirstName());
        visualUser.setLastName(user.getLastName());
        visualUser.setEmail(user.getEmail());
        visualUser.setOrganizationName(user.getOrganizationName());
        visualUser.setRole(user.getRole());
        visualUser.setCreatedAt(user.getCreatedAt());
        visualUser.setUpdatedAt(user.getUpdatedAt());

        return new ResponseEntity<>(visualUser, HttpStatus.OK);
    }
}
