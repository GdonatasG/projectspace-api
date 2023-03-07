package com.projectspace.projectspaceapi.invitation.controller;


import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.invitation.request.InvitationRequest;
import com.projectspace.projectspaceapi.invitation.service.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
