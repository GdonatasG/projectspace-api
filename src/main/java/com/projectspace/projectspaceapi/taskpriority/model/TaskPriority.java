package com.projectspace.projectspaceapi.taskpriority.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectspace.projectspaceapi.task.model.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "task_priority")
@NoArgsConstructor
public class TaskPriority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "priority")
    private List<Task> tasks;
}
