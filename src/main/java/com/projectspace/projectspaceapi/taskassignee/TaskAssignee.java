package com.projectspace.projectspaceapi.taskassignee;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.task.model.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "task_assignee", uniqueConstraints =
        {@UniqueConstraint(name = "task_assignee_unique", columnNames = {"member_id", "task_id"})})
@NoArgsConstructor
public class TaskAssignee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id")
    @JsonIgnoreProperties("project")
    private ProjectMember projectMember;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id")
    private Task task;
}
