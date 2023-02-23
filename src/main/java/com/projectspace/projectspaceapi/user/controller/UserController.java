package com.projectspace.projectspaceapi.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
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
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization").split(" ")[1];

        DecodedJWT verify = JWT.require(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()))
                .build()
                .verify(token.replace(AuthenticationConfigConstants.TOKEN_PREFIX, ""));

        User user = userService.readUserByUsername(verify.getSubject());

        VisualUser visualUser = new VisualUser();
        visualUser.setUsername(user.getUsername());
        visualUser.setEmail(user.getEmail());
        visualUser.setRole(user.getRole());

        return new ResponseEntity<>(visualUser, HttpStatus.OK);
    }
}
