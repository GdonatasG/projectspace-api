package com.projectspace.projectspaceapi.invitation.controller;


import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.common.response.SuccessBodyList;
import com.projectspace.projectspaceapi.invitation.model.Invitation;
import com.projectspace.projectspaceapi.invitation.request.InvitationRequest;
import com.projectspace.projectspaceapi.invitation.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.PROJECT_INVITATIONS_URL)
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<SuccessBody> invite(@RequestBody @Valid InvitationRequest invitationRequest) {
        invitationService.invite(invitationRequest);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<SuccessBodyList<Invitation>> getCurrentUserInvitations() {
        List<Invitation> invitations = invitationService.getCurrentUserInvitations();

        return new ResponseEntity<>(new SuccessBodyList<>(invitations), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<SuccessBodyList<Invitation>> getProjectInvitations(@RequestParam("project_id") int projectId) {
        List<Invitation> invitations = invitationService.getProjectInvitations(Integer.toUnsignedLong(projectId));

        return new ResponseEntity<>(new SuccessBodyList<>(invitations), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessBody> delete(@RequestParam("id") int id) {
        invitationService.deleteInvitation(Integer.toUnsignedLong(id));

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }

}
