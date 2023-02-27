package com.projectspace.projectspaceapi.project.service;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import com.projectspace.projectspaceapi.project.request.CreateProjectRequest;
import com.projectspace.projectspaceapi.user.model.User;
import com.projectspace.projectspaceapi.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public Project readById(Long id) {
        return projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void createProject(CreateProjectRequest createProjectRequest) {
        Optional<Project> byName = projectRepository.findByName(createProjectRequest.getName());

        if (byName.isPresent()) {
            throw new AlreadyTakenException("Project name is already taken!");
        }

        User currentUser = AuthenticationUserHelper.getCurrentUser(userService);

        Project project = new Project();

        project.setOwner(currentUser);
        project.setName(createProjectRequest.getName());
        project.setDescription(createProjectRequest.getDescription());

        projectRepository.save(project);
    }
}
