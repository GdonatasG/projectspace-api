package com.projectspace.projectspaceapi.task.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UpdateTaskRequest {
    @Nullable
    @Size(min = 1, max = 100, message = "The title should not be empty and should not exceed 100 characters.")
    private String title;

    @Nullable
    @Size(max = 3000, message = "The description should not exceed 3000 characters.")
    private String description;

    @Nullable
    private Long priority_id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start_date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime end_date;

    @Nullable
    private Set<Long> assignees;
}
