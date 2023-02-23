package com.projectspace.projectspaceapi.user.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class VisualUser {
    private String username;
    private String email;
    private String role;
}
