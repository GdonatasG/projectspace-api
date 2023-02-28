package com.projectspace.projectspaceapi.project.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.common.response.SuccessBodyList;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.request.CreateProjectRequest;
import com.projectspace.projectspaceapi.project.request.DeleteProjectRequest;
import com.projectspace.projectspaceapi.project.request.UpdateProjectDescriptionRequest;
import com.projectspace.projectspaceapi.project.request.UpdateProjectRequest;
import com.projectspace.projectspaceapi.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.PROJECT_URL)
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/{id}")
    public ResponseEntity<SuccessBody<Project>> getById(@PathVariable int id) {
        Project project = projectService.readById(Integer.toUnsignedLong(id));

        SuccessBody<Project> response = new SuccessBody<>(project);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SuccessBodyList<Project>> getProjects(
            @RequestParam(name = "owner_id", required = false) Long ownerId,
            @RequestParam(name = "not_current_user", defaultValue = "false") Boolean notCurrentUser) {
        List<Project> projects = projectService.getProjects(ownerId, notCurrentUser);

        SuccessBodyList<Project> response = new SuccessBodyList<>(projects);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SuccessBody> createProject(@RequestBody @Valid CreateProjectRequest createProjectRequest) {
        projectService.createProject(createProjectRequest);

        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SuccessBody> updateProject(@RequestBody @Valid UpdateProjectRequest updateProjectRequest) {
        projectService.updateProject(updateProjectRequest);

        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @PutMapping("/description")
    public ResponseEntity<SuccessBody> updateProjectDescription(@RequestBody @Valid UpdateProjectDescriptionRequest updateProjectDescriptionRequest) {
        projectService.updateProjectDescription(updateProjectDescriptionRequest);

        return new ResponseEntity<>(new SuccessBody(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessBody> deleteProject(@RequestBody @Valid DeleteProjectRequest deleteProjectRequest) {
        projectService.deleteProject(deleteProjectRequest);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }

}
