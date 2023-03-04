package com.projectspace.projectspaceapi.projectmember.service;

import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmember.repository.ProjectMemberRepository;
import com.projectspace.projectspaceapi.projectmember.request.DeleteMemberRequest;
import com.projectspace.projectspaceapi.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    private final ProjectRepository projectRepository;
    private final AuthenticationUserHelper authenticationUserHelper;


    @Transactional
    public void delete(DeleteMemberRequest deleteMemberRequest) {
        Optional<Project> project = projectRepository.findById(deleteMemberRequest.getProjectId());
        Optional<ProjectMember> projectMember = projectMemberRepository.findById(deleteMemberRequest.getMemberId());

        if (project.isEmpty()) {
            throw new ForbiddenException();
        }

        if (projectMember.isEmpty()) {
            throw new ForbiddenException();
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        if (!project.get().getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        if (projectMember.get().getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        projectMemberRepository.delete(projectMember.get());
    }
}
