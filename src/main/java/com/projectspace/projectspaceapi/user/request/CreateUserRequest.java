package com.projectspace.projectspaceapi.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotNull(message = "Username is required.")
    @Size(min = 5, max = 100, message = "The length of username must be between 5 and 100 characters.")
    private String username;

    @NotNull(message = "First name is required.")
    @Size(min = 1, max = 100, message = "The first name should not be empty and should not exceed 100 characters.")
    @JsonProperty("first_name")
    private String firstName;

    @NotNull(message = "Last name is required.")
    @Size(min = 1, max = 100, message = "The last name should not be empty and should not exceed 100 characters.")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "The email address is invalid.")
    private String email;

    @Nullable
    @Size(min = 1, max = 100, message = "The organization name should not be empty and should not exceed 100 characters.")
    @JsonProperty("organization_name")
    private String organizationName;

    @NotNull(message = "Password is required.")
    @Size(min = 5, max = 100, message = "The length of password must be between 5 and 100 characters.")
    private String password;

    @NotNull(message = "Role is required.")
    private String role;
}
