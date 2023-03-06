package com.projectspace.projectspaceapi.projectmemberlevel.controller;


import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBodyList;
import com.projectspace.projectspaceapi.projectmemberlevel.model.ProjectMemberLevel;
import com.projectspace.projectspaceapi.projectmemberlevel.service.ProjectMemberLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.PROJECT_MEMBER_LEVEL_URL)
@RequiredArgsConstructor
public class ProjectMemberLevelController {

    private final ProjectMemberLevelService projectMemberLevelService;

    @GetMapping
    public  ResponseEntity<SuccessBodyList<ProjectMemberLevel>> getAvailable() {
        List<ProjectMemberLevel> levels = projectMemberLevelService.getAvailable();

        return new ResponseEntity<>(new SuccessBodyList<>(levels), HttpStatus.OK);
    }
}
