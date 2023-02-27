package com.projectspace.projectspaceapi.project.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProjectDescriptionRequest {
    @NotNull(message = "Project id is required.")
    private Long projectId;
    @Nullable
    @Size(min = 1, max = 1000, message = "The description should not be empty and should not exceed 1000 characters.")
    private String description;
}
