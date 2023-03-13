package com.projectspace.projectspaceapi.task.service;

import com.projectspace.projectspaceapi.common.Formatter;
import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmember.repository.ProjectMemberRepository;
import com.projectspace.projectspaceapi.task.model.Task;
import com.projectspace.projectspaceapi.task.repository.TaskRepository;
import com.projectspace.projectspaceapi.task.request.CreateTaskRequest;
import com.projectspace.projectspaceapi.task.request.UpdateTaskRequest;
import com.projectspace.projectspaceapi.taskassignee.TaskAssignee;
import com.projectspace.projectspaceapi.taskassignee.TaskAssigneeRepository;
import com.projectspace.projectspaceapi.taskpriority.model.TaskPriority;
import com.projectspace.projectspaceapi.taskstatus.model.TaskStatus;
import com.projectspace.projectspaceapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    private final TaskAssigneeRepository taskAssigneeRepository;

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

    @Transactional
    public void createTask(CreateTaskRequest createTaskRequest) {
        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> member =
                projectMemberRepository.findByProjectIdAndUserId(createTaskRequest.getProject_id(), currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        TaskStatus status = new TaskStatus();
        status.setId(1L);

        TaskPriority taskPriority = new TaskPriority();
        taskPriority.setId(createTaskRequest.getPriority_id());

        Project project = new Project();
        project.setId(createTaskRequest.getProject_id());

        Task task = new Task();
        task.setTitle(createTaskRequest.getTitle());
        task.setDescription(createTaskRequest.getDescription());
        task.setStatus(status);
        task.setPriority(taskPriority);
        if (createTaskRequest.getStart_date() != null) {
            task.setStartDate(createTaskRequest.getStart_date());
        }
        if (createTaskRequest.getEnd_date() != null) {
            task.setEndDate(createTaskRequest.getEnd_date());
        }
        task.setProject(project);

        taskRepository.save(task);

        if (createTaskRequest.getAssignees() != null) {
            for (Long memberId : createTaskRequest.getAssignees()) {
                Optional<ProjectMember> projectMember = projectMemberRepository.findById(memberId);

                if (projectMember.isEmpty()) {
                    throw new NotFoundException("Project member with id " + memberId + " not found!");
                }

                if (!projectMember.get().getProject().getId().equals(task.getProject().getId())) {
                    throw new ForbiddenException();
                }

                TaskAssignee assignee = new TaskAssignee();
                assignee.setProjectMember(projectMember.get());
                assignee.setTask(task);

                taskAssigneeRepository.save(assignee);

            }
        }
    }

    public void changeTaskStatus(Long taskId, Boolean shouldClose) {
        Optional<Task> task = taskRepository.findById(taskId);

        if (task.isEmpty()) {
            throw new NotFoundException("Task not found!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> member =
                projectMemberRepository.findByProjectIdAndUserId(task.get().getProject().getId(), currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        TaskStatus status = new TaskStatus();

        if (shouldClose) {
            status.setId(2L);
        } else {
            status.setId(1L);
        }

        task.get().setStatus(status);

        taskRepository.save(task.get());
    }

    public void updateTask(Long taskId, UpdateTaskRequest updateTaskRequest) {
        Optional<Task> task = taskRepository.findById(taskId);

        if (task.isEmpty()) {
            throw new NotFoundException("Task not found!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> member =
                projectMemberRepository.findByProjectIdAndUserId(task.get().getProject().getId(), currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        if (updateTaskRequest.getTitle() != null) {
            task.get().setTitle(updateTaskRequest.getTitle());
        }

        if (updateTaskRequest.getDescription() != null) {
            task.get().setDescription(updateTaskRequest.getDescription());
        }

        if (updateTaskRequest.getPriority_id() != null) {
            TaskPriority priority = new TaskPriority();
            priority.setId(updateTaskRequest.getPriority_id());
            task.get().setPriority(priority);
        }

        if (updateTaskRequest.getStart_date() != null) {
            task.get().setStartDate(updateTaskRequest.getStart_date());
        }

        if (updateTaskRequest.getEnd_date() != null) {
            task.get().setEndDate(updateTaskRequest.getEnd_date());
        }

        if (updateTaskRequest.getAssignees() != null) {
            System.out.println(updateTaskRequest.getAssignees().size());
            // TODO: save new assignees
        }

        taskRepository.save(task.get());
    }
}
