package com.projectspace.projectspaceapi.taskstatus.repository;

import com.projectspace.projectspaceapi.taskstatus.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
}
