package com.projectspace.projectspaceapi.project.service;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import com.projectspace.projectspaceapi.project.request.CreateProjectRequest;
import com.projectspace.projectspaceapi.project.request.DeleteProjectRequest;
import com.projectspace.projectspaceapi.project.request.UpdateProjectRequest;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMemberLevel;
import com.projectspace.projectspaceapi.projectmember.service.ProjectMemberService;
import com.projectspace.projectspaceapi.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMemberService projectMemberService;

    private final AuthenticationUserHelper authenticationUserHelper;

    public Project readById(Long id) {
        return projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Project> getProjects(Long ownerId, Boolean notCurrentUser) {
        if (ownerId != null) {
            return projectRepository.findAllByOwnerId(ownerId);
        }

        if (notCurrentUser) {
            return projectRepository.findAllByOwnerIdNot(authenticationUserHelper.getCurrentUser().getId());
        }

        return projectRepository.findAll();
    }

    public List<Project> getUserAvailableProjects() {
        return projectRepository.findAllByProjectMembers_UserId(authenticationUserHelper.getCurrentUser().getId());
    }

    @Transactional
    public void createProject(CreateProjectRequest createProjectRequest) {
        Optional<Project> byName = projectRepository.findByName(createProjectRequest.getName());

        if (byName.isPresent()) {
            throw new AlreadyTakenException("Project name is already taken!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        Project project = new Project();

        project.setOwner(currentUser);
        project.setName(createProjectRequest.getName());
        project.setDescription(createProjectRequest.getDescription());

        ProjectMemberLevel projectMemberLevel = new ProjectMemberLevel();
        projectMemberLevel.setId(1L);


        ProjectMember projectMember = new ProjectMember();
        projectMember.setUser(currentUser);
        projectMember.setProject(project);
        projectMember.setLevel(projectMemberLevel);

        projectRepository.save(project);
        projectMemberService.createMember(projectMember);
    }

    public void updateProject(UpdateProjectRequest updateProjectRequest) {
        Optional<Project> byId = projectRepository.findById(updateProjectRequest.getProjectId());

        if (byId.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        Project project = byId.get();
        User currentUser = authenticationUserHelper.getCurrentUser();


        if (project.getOwner() == null || !project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        if (updateProjectRequest.getName() != null
        ) {
            String name = updateProjectRequest.getName();
            Optional<Project> byName = projectRepository.findByName(name);

            if (byName.isPresent()) {
                throw new AlreadyTakenException("Project name is already taken!");
            }

            project.setName(updateProjectRequest.getName());
        }

        if (updateProjectRequest.getDescription() != null) {
            project.setDescription(updateProjectRequest.getDescription());
        }

        projectRepository.save(project);
    }

    public void deleteProject(DeleteProjectRequest deleteProjectRequest) {
        Optional<Project> byId = projectRepository.findById(deleteProjectRequest.getProjectId());

        if (byId.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        Project project = byId.get();
        User currentUser = authenticationUserHelper.getCurrentUser();

        if (project.getOwner() == null || !project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        projectRepository.delete(project);
    }
}
