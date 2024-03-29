package com.projectspace.projectspaceapi.projectmember.service;

import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmemberlevel.model.ProjectMemberLevel;
import com.projectspace.projectspaceapi.projectmember.repository.ProjectMemberRepository;
import com.projectspace.projectspaceapi.projectmember.request.DeleteMemberRequest;
import com.projectspace.projectspaceapi.projectmember.request.UpdateProjectMemberRequest;
import com.projectspace.projectspaceapi.task.model.Task;
import com.projectspace.projectspaceapi.task.repository.TaskRepository;
import com.projectspace.projectspaceapi.taskassignee.repository.TaskAssigneeRepository;
import com.projectspace.projectspaceapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    private final ProjectRepository projectRepository;

    private final TaskAssigneeRepository taskAssigneeRepository;

    private final TaskRepository taskRepository;
    private final AuthenticationUserHelper authenticationUserHelper;


    @Transactional
    public void delete(DeleteMemberRequest deleteMemberRequest) {
        Optional<Project> project = projectRepository.findById(deleteMemberRequest.getProject_id());
        Optional<ProjectMember> projectMember = projectMemberRepository.findById(deleteMemberRequest.getMember_id());

        if (project.isEmpty()) {
            throw new ForbiddenException();
        }

        if (projectMember.isEmpty()) {
            throw new ForbiddenException();
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        if (!project.get().getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        Optional<ProjectMember> projectOwner = projectMemberRepository.findByProjectIdAndUserId(project.get().getId(), project.get().getOwner().getId());

        if (projectOwner.isEmpty()) {
            throw new ForbiddenException();
        }

        if (projectMember.get().getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        List<Task> memberTasks = taskRepository.findAllByProject_IdAndCreator_Id(project.get().getId(), projectMember.get().getId());
        for (Task task : memberTasks) {
            task.setCreator(projectOwner.get());
            taskRepository.save(task);
        }

        taskAssigneeRepository.deleteAllByProjectMember_Id(projectMember.get().getId());
        projectMemberRepository.delete(projectMember.get());
    }

    public void update(UpdateProjectMemberRequest updateProjectMemberRequest) {
        Optional<ProjectMember> member = projectMemberRepository.findById(updateProjectMemberRequest.getMember_id());

        if (member.isEmpty()) {
            throw new NotFoundException("Project member not found!");
        }

        Optional<Project> project = projectRepository.findById(member.get().getProject().getId());

        if (project.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        if (!project.get().getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        if (member.get().getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        if (updateProjectMemberRequest.getMember_level_id() == null) {
            return;
        }

        if (updateProjectMemberRequest.getMember_level_id() == 1) {
            throw new ForbiddenException();
        }

        ProjectMemberLevel newLevel = new ProjectMemberLevel();
        newLevel.setId(updateProjectMemberRequest.getMember_level_id());
        member.get().setLevel(newLevel);

        projectMemberRepository.save(member.get());
    }

    public List<ProjectMember> getProjectMembers(Long projectId) {
        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> member = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        return projectMemberRepository.findAllByProjectId(projectId, ProjectMember.SORT_BY_MEMBER_LEVEL_ASC);
    }
}
