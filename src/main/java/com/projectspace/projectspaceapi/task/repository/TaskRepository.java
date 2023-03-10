package com.projectspace.projectspaceapi.task.repository;

import com.projectspace.projectspaceapi.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
