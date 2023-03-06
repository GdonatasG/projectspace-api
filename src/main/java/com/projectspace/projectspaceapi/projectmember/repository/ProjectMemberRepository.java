package com.projectspace.projectspaceapi.projectmember.repository;

import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findAllByProjectId(Long projectId, Sort sort);

    List<ProjectMember> findAllByUserId(Long userId);

    Optional<ProjectMember> findByProjectIdAndUserId(Long projectId, Long userId);

    void deleteAllByProjectId(Long projectId);
}
