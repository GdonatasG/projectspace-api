package com.projectspace.projectspaceapi.project.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetProjectsByOwnerIdRequest {
    @NotNull(message = "OwnerId is required.")
    private Long ownerId;
}
