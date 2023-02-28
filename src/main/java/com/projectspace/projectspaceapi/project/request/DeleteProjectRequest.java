package com.projectspace.projectspaceapi.project.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteProjectRequest {
    @NotNull(message = "Project id is required.")
    private Long projectId;
}
