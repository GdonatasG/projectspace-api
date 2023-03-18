package com.projectspace.projectspaceapi.projectmember.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.projectmemberlevel.model.ProjectMemberLevel;
import com.projectspace.projectspaceapi.task.model.Task;
import com.projectspace.projectspaceapi.taskassignee.model.TaskAssignee;
import com.projectspace.projectspaceapi.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "project_member", uniqueConstraints =
        {@UniqueConstraint(name = "project_member_unique", columnNames = {"user_id", "project_id"})})
@NoArgsConstructor
public class ProjectMember {

    @Transient
    public static final Sort SORT_BY_MEMBER_LEVEL_ASC =
            Sort.by(Sort.Direction.ASC, "level.id");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_level")
    private ProjectMemberLevel level;

    @JsonIgnore
    @OneToMany(mappedBy = "projectMember")
    private List<TaskAssignee> assigned;

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<Task> tasksOwned;
}
