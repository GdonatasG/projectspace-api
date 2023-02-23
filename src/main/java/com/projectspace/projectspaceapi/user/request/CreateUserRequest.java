package com.projectspace.projectspaceapi.user.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotNull(message = "Username is required.")
    @Size(min = 5, max = 150, message = "The length of username must be between 5 and 150 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "The email address is invalid.")
    private String email;

    @NotNull(message = "Password is required.")
    @Size(min = 5, max = 100, message = "The length of password must be between 5 and 100 characters.")
    private String password;
}
