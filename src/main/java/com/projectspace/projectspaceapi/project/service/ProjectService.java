package com.projectspace.projectspaceapi.project.service;

import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project readById(Long id) {
        return projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
