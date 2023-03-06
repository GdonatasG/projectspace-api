package com.projectspace.projectspaceapi.projectmemberlevel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "project_member_level")
@NoArgsConstructor
public class ProjectMemberLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "level")
    private List<ProjectMember> projectMembers;
}
