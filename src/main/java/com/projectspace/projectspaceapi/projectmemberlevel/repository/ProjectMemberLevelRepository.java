package com.projectspace.projectspaceapi.projectmemberlevel.repository;

import com.projectspace.projectspaceapi.projectmemberlevel.model.ProjectMemberLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberLevelRepository extends JpaRepository<ProjectMemberLevel, Long> {
    List<ProjectMemberLevel> findAllByNameNot(String name);
}
