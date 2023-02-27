package com.projectspace.projectspaceapi.project.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotNull(message = "Name is required.")
    @Size(min = 1, max = 100, message = "The name should not be empty and should not exceed 100 characters.")
    private String name;

    @Nullable
    @Size(min = 1, max = 1000, message = "The description should not be empty and should not exceed 1000 characters.")
    private String description;
}
