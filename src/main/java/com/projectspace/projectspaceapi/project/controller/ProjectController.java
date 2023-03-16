package com.projectspace.projectspaceapi.project.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.common.response.SuccessBodyList;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.model.ProjectStatistics;
import com.projectspace.projectspaceapi.project.request.CreateProjectRequest;
import com.projectspace.projectspaceapi.project.request.DeleteProjectRequest;
import com.projectspace.projectspaceapi.project.request.UpdateProjectRequest;
import com.projectspace.projectspaceapi.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.PROJECT_URL)
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<SuccessBody<Project>> getById(@PathVariable int id) {
        Project project = projectService.getByIdIfCurrentUserIsProjectMember(Integer.toUnsignedLong(id));

        SuccessBody<Project> response = new SuccessBody<>(project);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/statistics")
    public ResponseEntity<SuccessBody<ProjectStatistics>> getProjectStatistics(@PathVariable int id) {
        ProjectStatistics statistics = projectService.getProjectStatistics(Integer.toUnsignedLong(id));

        SuccessBody<ProjectStatistics> response = new SuccessBody<>(statistics);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<SuccessBodyList<Project>> getUserAvailableProjects() {
        List<Project> projects = projectService.getUserAvailableProjects();

        SuccessBodyList<Project> response = new SuccessBodyList<>(projects);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SuccessBody> createProject(@RequestBody @Valid CreateProjectRequest createProjectRequest) throws Exception {
        projectService.createProject(createProjectRequest);

        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SuccessBody> updateProject(@RequestBody @Valid UpdateProjectRequest updateProjectRequest) {
        projectService.updateProject(updateProjectRequest);

        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessBody> deleteProject(@RequestBody @Valid DeleteProjectRequest deleteProjectRequest) {
        projectService.deleteProject(deleteProjectRequest);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }

}
