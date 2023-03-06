package com.projectspace.projectspaceapi.projectmemberlevel.service;

import com.projectspace.projectspaceapi.projectmemberlevel.repository.ProjectMemberLevelRepository;
import com.projectspace.projectspaceapi.projectmemberlevel.model.ProjectMemberLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberLevelService {
    private final ProjectMemberLevelRepository projectMemberLevelRepository;

    public List<ProjectMemberLevel> getAvailable() {
        return projectMemberLevelRepository.findAllByNameNot("OWNER");
    }
}
