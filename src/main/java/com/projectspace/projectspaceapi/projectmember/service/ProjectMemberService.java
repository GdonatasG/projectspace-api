package com.projectspace.projectspaceapi.projectmember.service;

import com.projectspace.projectspaceapi.projectmember.ProjectMember;
import com.projectspace.projectspaceapi.projectmember.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {
    private final ProjectMemberRepository projectMemberRepository;

    @Transactional
    public void createMember(ProjectMember projectMember) {
        projectMemberRepository.save(projectMember);
    }
}
