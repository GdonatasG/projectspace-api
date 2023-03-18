package com.projectspace.projectspaceapi.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectRisk {
    @JsonProperty(value = "total_tasks")
    private Long totalTasks;

    @JsonProperty(value = "open_tasks")
    private Long openTasks;

    private String risk;
}
