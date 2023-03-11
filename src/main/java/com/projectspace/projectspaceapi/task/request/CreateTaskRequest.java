package com.projectspace.projectspaceapi.task.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class CreateTaskRequest {
    @NotNull(message = "project_id is required.")
    private Long project_id;

    @NotNull(message = "title is required.")
    @Size(min = 1, max = 100, message = "The title should not be empty and should not exceed 100 characters.")
    private String title;

    @Nullable
    @Size(min = 1, max = 3000, message = "The description should not be empty and should not exceed 3000 characters.")
    private String description;

    @NotNull(message = "priority_id is required.")
    private Long priority_id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start_date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime end_date;

    @Nullable
    private Set<Long> assignees;
}
