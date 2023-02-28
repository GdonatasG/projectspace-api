package com.projectspace.projectspaceapi.project.service;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import com.projectspace.projectspaceapi.project.request.CreateProjectRequest;
import com.projectspace.projectspaceapi.project.request.DeleteProjectRequest;
import com.projectspace.projectspaceapi.project.request.UpdateProjectDescriptionRequest;
import com.projectspace.projectspaceapi.project.request.UpdateProjectRequest;
import com.projectspace.projectspaceapi.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AuthenticationUserHelper authenticationUserHelper;

    public Project readById(Long id) {
        return projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Project> getCurrentUserProjects() {
        User currentUser = authenticationUserHelper.getCurrentUser();
        return projectRepository.findAllByOwnerId(currentUser.getId());
    }

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

        // TODO: Add current user as a project member into ProjectMember table using transactions

        projectRepository.save(project);
    }

    public void updateProject(UpdateProjectRequest updateProjectRequest) {
        Optional<Project> byId = projectRepository.findById(updateProjectRequest.getProjectId());

        if (byId.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        Project project = byId.get();
        User currentUser = authenticationUserHelper.getCurrentUser();

        if (!project.getOwner().getId().equals(currentUser.getId())) {
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

        projectRepository.save(project);
    }

    public void updateProjectDescription(UpdateProjectDescriptionRequest updateProjectDescriptionRequest) {
        Optional<Project> byId = projectRepository.findById(updateProjectDescriptionRequest.getProjectId());

        if (byId.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        Project project = byId.get();
        User currentUser = authenticationUserHelper.getCurrentUser();

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        if (updateProjectDescriptionRequest.getDescription() != null) {
            project.setDescription(updateProjectDescriptionRequest.getDescription());
        } else {
            project.setDescription(null);
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

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        projectRepository.delete(project);
    }
}
