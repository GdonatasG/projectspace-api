package com.projectspace.projectspaceapi.user.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.user.model.User;
import com.projectspace.projectspaceapi.user.request.CreateUserRequest;
import com.projectspace.projectspaceapi.user.request.UpdateOrganizationRequest;
import com.projectspace.projectspaceapi.user.request.UpdateUserRequest;
import com.projectspace.projectspaceapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest) {
        userService.updateUser(updateUserRequest);
        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @PutMapping("/organization")
    public ResponseEntity updateOrganization(@RequestBody @Valid UpdateOrganizationRequest updateOrganizationRequest) {
        userService.updateOrganization(updateOrganizationRequest);
        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @GetMapping("/test")
    // TODO: delete, just for testing purposes
    public ResponseEntity test() {
        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<User> getCurrentUser() {
        User user = AuthenticationUserHelper.getCurrentUser(userService);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
