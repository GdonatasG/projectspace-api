package com.projectspace.projectspaceapi.task.service;

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
import com.projectspace.projectspaceapi.taskassignee.model.TaskAssignee;
import com.projectspace.projectspaceapi.taskassignee.repository.TaskAssigneeRepository;
import com.projectspace.projectspaceapi.taskpriority.model.TaskPriority;
import com.projectspace.projectspaceapi.taskstatus.model.TaskStatus;
import com.projectspace.projectspaceapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public List<Task> getUserAssignedTasksByProjectId(Long projectId) {
        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> member =
                projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        return taskRepository.findAllByProject_IdAndAssignees_ProjectMember_Id(projectId, member.get().getId());
    }

    public List<Task> getProjectTasks(Long projectId) {
        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> member =
                projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        return taskRepository.findAllByProject_Id(projectId);
    }

    @Transactional
    public void createTask(CreateTaskRequest createTaskRequest) {
        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> optionalMember =
                projectMemberRepository.findByProjectIdAndUserId(createTaskRequest.getProject_id(), currentUser.getId());

        if (optionalMember.isEmpty()) {
            throw new ForbiddenException();
        }

        ProjectMember member = optionalMember.get();

        TaskStatus status = new TaskStatus();
        status.setId(1L);

        TaskPriority taskPriority = new TaskPriority();
        taskPriority.setId(createTaskRequest.getPriority_id());

        Project project = new Project();
        project.setId(createTaskRequest.getProject_id());

        Task task = new Task();
        task.setTitle(createTaskRequest.getTitle());
        task.setDescription(createTaskRequest.getDescription());
        task.setCreator(member);
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

        if (createTaskRequest.getAssignees() == null) {
            return;
        }

        for (Long memberId : createTaskRequest.getAssignees()) {
            saveAssignee(memberId, task);
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

    @Transactional
    public void updateTask(Long taskId, UpdateTaskRequest updateTaskRequest) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isEmpty()) {
            throw new NotFoundException("Task not found!");
        }

        Task task = optionalTask.get();


        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> member =
                projectMemberRepository.findByProjectIdAndUserId(task.getProject().getId(), currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        if (updateTaskRequest.getTitle() != null) {
            task.setTitle(updateTaskRequest.getTitle());
        }

        if (updateTaskRequest.getDescription() != null) {
            task.setDescription(updateTaskRequest.getDescription());
        }

        if (updateTaskRequest.getPriority_id() != null) {
            TaskPriority priority = new TaskPriority();
            priority.setId(updateTaskRequest.getPriority_id());
            task.setPriority(priority);
        }

        if (updateTaskRequest.getStart_date() != null) {
            task.setStartDate(updateTaskRequest.getStart_date());
        }

        if (updateTaskRequest.getEnd_date() != null) {
            task.setEndDate(updateTaskRequest.getEnd_date());
        }

        taskRepository.save(task);

        if (updateTaskRequest.getAssignees() == null) {
            return;
        }

        taskAssigneeRepository.deleteAllByTaskId(taskId);
        taskAssigneeRepository.flush();

        for (Long memberId : updateTaskRequest.getAssignees()) {
            saveAssignee(memberId, task);
        }
    }


    private void saveAssignee(Long memberId, Task task) {
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
