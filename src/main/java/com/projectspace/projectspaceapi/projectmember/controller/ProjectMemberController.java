package com.projectspace.projectspaceapi.projectmember.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.projectmember.request.DeleteMemberRequest;
import com.projectspace.projectspaceapi.projectmember.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.PROJECT_MEMBER_URL)
@RequiredArgsConstructor
public class ProjectMemberController {
    private final ProjectMemberService projectMemberService;

    @DeleteMapping
    public ResponseEntity<SuccessBody> deleteMember(@RequestBody @Valid DeleteMemberRequest deleteMemberRequest) {
        projectMemberService.delete(deleteMemberRequest);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }
}
