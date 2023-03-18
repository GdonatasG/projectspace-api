package com.projectspace.projectspaceapi.project.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteProjectRequest {
    @NotNull(message = "project_id is required.")
    private Long project_id;
}
