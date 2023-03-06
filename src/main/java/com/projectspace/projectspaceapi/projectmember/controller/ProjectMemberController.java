package com.projectspace.projectspaceapi.projectmember.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.common.response.SuccessBodyList;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmember.request.DeleteMemberRequest;
import com.projectspace.projectspaceapi.projectmember.request.UpdateProjectMemberRequest;
import com.projectspace.projectspaceapi.projectmember.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/update")
    public ResponseEntity<SuccessBody> updateMember(@RequestBody @Valid UpdateProjectMemberRequest updateProjectMemberRequest) {
        projectMemberService.update(updateProjectMemberRequest);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SuccessBodyList<ProjectMember>> getProjectMembers(@RequestParam("project_id") int projectId) {
        List<ProjectMember> members = projectMemberService.getProjectMembers(Integer.toUnsignedLong(projectId));

        return new ResponseEntity<>(new SuccessBodyList<>(members), HttpStatus.OK);
    }
}
