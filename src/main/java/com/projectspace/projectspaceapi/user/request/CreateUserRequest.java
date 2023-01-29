package com.projectspace.projectspaceapi.user.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
}
