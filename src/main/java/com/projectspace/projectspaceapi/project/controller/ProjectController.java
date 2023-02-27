package com.projectspace.projectspaceapi.project.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.request.CreateProjectRequest;
import com.projectspace.projectspaceapi.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<SuccessBody> createProject(@RequestBody @Valid CreateProjectRequest createProjectRequest) {
        projectService.createProject(createProjectRequest);

        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }
}
