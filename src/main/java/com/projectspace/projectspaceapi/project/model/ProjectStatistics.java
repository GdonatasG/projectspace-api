package com.projectspace.projectspaceapi.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProjectStatistics {
    @JsonProperty(value = "total_tasks")
    private Long totalTasks;

    @JsonProperty(value = "open_tasks")
    private Long openTasks;

    @JsonProperty(value = "closed_tasks")
    private Long closedTasks;
}
