package com.projectspace.projectspaceapi.task.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.taskpriority.model.TaskPriority;
import com.projectspace.projectspaceapi.taskstatus.model.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "task")
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private TaskPriority priority;

    @Column(name = "start_date")
    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    @JsonProperty("end_date")
    private LocalDateTime endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

}
