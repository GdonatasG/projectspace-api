package com.projectspace.projectspaceapi.project.repository;

import com.projectspace.projectspaceapi.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);

    List<Project> findAllByOwnerId(Long id);

    List<Project> findAllByOwnerIdNot(Long id);

    List<Project> findAllByProjectMembers_UserId(Long userId);

    List<Project> findAllByProjectMembers_UserIdAndOwnerIdNot(Long userId, Long ownerId);
}
