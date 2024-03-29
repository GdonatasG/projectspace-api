package com.projectspace.projectspaceapi.invitation.model;


import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invitation", uniqueConstraints =
        {@UniqueConstraint(name = "invitation_unique", columnNames = {"user_id", "project_id"})})
@NoArgsConstructor
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    private Project project;
}
