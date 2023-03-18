package com.projectspace.projectspaceapi.invitation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InvitationRequest {
    @NotBlank(message = "email is required.")
    @Email(message = "The email address is invalid.")
    private String email;

    @NotNull(message = "project_id is required.")
    private Long project_id;
}
