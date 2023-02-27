package com.projectspace.projectspaceapi.project.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.PROJECT_URL)
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<Project> getById(@PathVariable int id) {
        Project project = projectService.readById(Integer.toUnsignedLong(id));

        return new ResponseEntity<>(project, HttpStatus.OK);
    }
}
