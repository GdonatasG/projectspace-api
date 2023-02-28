package com.projectspace.projectspaceapi.project.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProjectRequest {

    @NotNull(message = "ProjectId is required.")
    private Long projectId;

    @Nullable
    @Size(min = 1, max = 100, message = "The name should not be empty and should not exceed 100 characters.")
    private String name;
}
