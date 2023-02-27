package com.projectspace.projectspaceapi.project.repository;

import com.projectspace.projectspaceapi.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
