package com.projectspace.projectspaceapi.taskpriority.service;

import com.projectspace.projectspaceapi.taskpriority.model.TaskPriority;
import com.projectspace.projectspaceapi.taskpriority.repository.TaskPriorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskPriorityService {
    private final TaskPriorityRepository taskPriorityRepository;

    public List<TaskPriority> getTaskPriorities() {
        return taskPriorityRepository.findAll();
    }
}
