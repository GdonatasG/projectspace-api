package com.projectspace.projectspaceapi.taskpriority.repository;

import com.projectspace.projectspaceapi.taskpriority.model.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Long> {
}
