package com.projectspace.projectspaceapi.task.repository;

import com.projectspace.projectspaceapi.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProject_IdAndAssignees_ProjectMember_Id(Long projectId, Long memberId);
}
