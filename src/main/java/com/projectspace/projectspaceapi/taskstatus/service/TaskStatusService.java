package com.projectspace.projectspaceapi.taskstatus.service;

import com.projectspace.projectspaceapi.taskstatus.model.TaskStatus;
import com.projectspace.projectspaceapi.taskstatus.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;

    public List<TaskStatus> getTaskStatuses() {
        return taskStatusRepository.findAll();
    }
}
