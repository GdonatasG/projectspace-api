package com.projectspace.projectspaceapi.invitation.service;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.invitation.model.Invitation;
import com.projectspace.projectspaceapi.invitation.repository.InvitationRepository;
import com.projectspace.projectspaceapi.invitation.request.InvitationRequest;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmember.repository.ProjectMemberRepository;
import com.projectspace.projectspaceapi.user.model.User;
import com.projectspace.projectspaceapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;

    private final ProjectRepository projectRepository;

    private final ProjectMemberRepository projectMemberRepository;

    private final UserRepository userRepository;

    private final AuthenticationUserHelper authenticationUserHelper;

    public void invite(InvitationRequest invitationRequest) {
        Optional<Project> project = projectRepository.findById(invitationRequest.getProjectId());

        if (project.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> currentUserAsProjectMember = projectMemberRepository.findByProjectIdAndUserId(project.get().getId(), currentUser.getId());

        if (currentUserAsProjectMember.isEmpty()) {
            throw new ForbiddenException();
        }

        String currentUserLevel = currentUserAsProjectMember.get().getLevel().getName();

        if (!Objects.equals(currentUserLevel, "OWNER") && !Objects.equals(currentUserLevel, "MODERATOR")) {
            throw new ForbiddenException();
        }

        Optional<User> invitedUser = userRepository.findByEmail(invitationRequest.getEmail());

        if (invitedUser.isEmpty()) {
            throw new NotFoundException("User with this email not found!");
        }

        Optional<ProjectMember> possibleMember = projectMemberRepository.findByProjectIdAndUserId(invitationRequest.getProjectId(), invitedUser.get().getId());


        if (possibleMember.isPresent()) {
            throw new AlreadyTakenException("User is already member!");
        }

        Optional<Invitation> possibleInvitation = invitationRepository.findByUserEmail(invitationRequest.getEmail());

        if (possibleInvitation.isPresent()) {
            throw new AlreadyTakenException("User is already invited!");
        }

        if (currentUser.getEmail().equals(invitationRequest.getEmail())) {
            throw new ForbiddenException();
        }

        Invitation invitation = new Invitation();

        invitation.setUser(invitedUser.get());
        invitation.setProject(project.get());

        invitationRepository.save(invitation);
    }

    public List<Invitation> getCurrentUserInvitations() {
        User currentUser = authenticationUserHelper.getCurrentUser();

        return invitationRepository.findAllByUser_Id(currentUser.getId());
    }

    public List<Invitation> getProjectInvitations(Long projectId) {
        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> currentUserAsProjectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId());

        if (currentUserAsProjectMember.isEmpty()) {
            throw new ForbiddenException();
        }

        String currentUserLevel = currentUserAsProjectMember.get().getLevel().getName();

        if (!Objects.equals(currentUserLevel, "OWNER") && !Objects.equals(currentUserLevel, "MODERATOR")) {
            throw new ForbiddenException();
        }

        return invitationRepository.findAllByProject_Id(projectId);
    }

    public void deleteInvitation(Long invitationId) {
        Optional<Invitation> invitation = invitationRepository.findById(invitationId);

        if (invitation.isEmpty()) {
            throw new NotFoundException("Invitation not found!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> currentUserAsProjectMember = projectMemberRepository.findByProjectIdAndUserId(invitation.get().getProject().getId(), currentUser.getId());

        if (currentUserAsProjectMember.isEmpty()) {
            throw new ForbiddenException();
        }

        String currentUserLevel = currentUserAsProjectMember.get().getLevel().getName();

        if (!Objects.equals(currentUserLevel, "OWNER") && !Objects.equals(currentUserLevel, "MODERATOR")) {
            throw new ForbiddenException();
        }

        invitationRepository.delete(invitation.get());
    }
}
