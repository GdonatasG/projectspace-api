package com.projectspace.projectspaceapi.task.service;

import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmember.repository.ProjectMemberRepository;
import com.projectspace.projectspaceapi.task.model.Task;
import com.projectspace.projectspaceapi.task.repository.TaskRepository;
import com.projectspace.projectspaceapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final ProjectMemberRepository projectMemberRepository;

    private final AuthenticationUserHelper authenticationUserHelper;

    public Task getTask(Long taskId) {
        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<Task> task = taskRepository.findById(taskId);

        if (task.isEmpty()) {
            throw new NotFoundException("Task not found!");
        }

        Optional<ProjectMember> member =
                projectMemberRepository.findByProjectIdAndUserId(task.get().getProject().getId(), currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        return task.get();
    }
}
