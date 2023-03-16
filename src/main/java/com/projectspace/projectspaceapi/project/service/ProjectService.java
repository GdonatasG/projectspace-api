package com.projectspace.projectspaceapi.project.service;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.invitation.repository.InvitationRepository;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.model.ProjectStatistics;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import com.projectspace.projectspaceapi.project.request.CreateProjectRequest;
import com.projectspace.projectspaceapi.project.request.DeleteProjectRequest;
import com.projectspace.projectspaceapi.project.request.UpdateProjectRequest;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmemberlevel.model.ProjectMemberLevel;
import com.projectspace.projectspaceapi.projectmember.repository.ProjectMemberRepository;
import com.projectspace.projectspaceapi.task.model.Task;
import com.projectspace.projectspaceapi.task.repository.TaskRepository;
import com.projectspace.projectspaceapi.taskassignee.repository.TaskAssigneeRepository;
import com.projectspace.projectspaceapi.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMemberRepository projectMemberRepository;

    private final AuthenticationUserHelper authenticationUserHelper;

    private final TaskRepository taskRepository;

    private final TaskAssigneeRepository taskAssigneeRepository;

    private final InvitationRepository invitationRepository;

    public Project getByIdIfCurrentUserIsProjectMember(Long id) {
        User currentUser = authenticationUserHelper.getCurrentUser();

        Optional<ProjectMember> projectMember = projectMemberRepository.findByProjectIdAndUserId(id, currentUser.getId());

        if (projectMember.isEmpty()) {
            throw new ForbiddenException();
        }

        return projectRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Project> getUserAvailableProjects() {
        return projectRepository.findAllByProjectMembers_UserId(authenticationUserHelper.getCurrentUser().getId());
    }

    @Transactional
    public void createProject(CreateProjectRequest createProjectRequest) {
        Optional<Project> byName = projectRepository.findByName(createProjectRequest.getName());

        if (byName.isPresent()) {
            throw new AlreadyTakenException("Project name is already taken!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();

        Project project = new Project();

        project.setOwner(currentUser);
        project.setName(createProjectRequest.getName());
        project.setDescription(createProjectRequest.getDescription());

        ProjectMemberLevel projectMemberLevel = new ProjectMemberLevel();
        projectMemberLevel.setId(1L);


        ProjectMember projectMember = new ProjectMember();
        projectMember.setUser(currentUser);
        projectMember.setProject(project);
        projectMember.setLevel(projectMemberLevel);

        projectRepository.save(project);
        projectMemberRepository.save(projectMember);
    }

    public void updateProject(UpdateProjectRequest updateProjectRequest) {
        Optional<Project> byId = projectRepository.findById(updateProjectRequest.getProjectId());

        if (byId.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        Project project = byId.get();
        User currentUser = authenticationUserHelper.getCurrentUser();


        if (project.getOwner() == null || !project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        if (updateProjectRequest.getName() != null
        ) {
            String name = updateProjectRequest.getName();
            Optional<Project> byName = projectRepository.findByName(name);

            if (byName.isPresent()) {
                throw new AlreadyTakenException("Project name is already taken!");
            }

            project.setName(updateProjectRequest.getName());
        }

        if (updateProjectRequest.getDescription() != null) {
            project.setDescription(updateProjectRequest.getDescription());
        }

        projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(DeleteProjectRequest deleteProjectRequest) {
        Optional<Project> byId = projectRepository.findById(deleteProjectRequest.getProject_id());

        if (byId.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        Project project = byId.get();
        User currentUser = authenticationUserHelper.getCurrentUser();

        if (project.getOwner() == null || !project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        taskAssigneeRepository.deleteAllByTask_Project_Id(project.getId());
        taskRepository.deleteAllByProject_Id(project.getId());
        invitationRepository.deleteAllByProject_Id(project.getId());
        projectMemberRepository.deleteAllByProjectId(project.getId());
        projectRepository.delete(project);
    }

    @SneakyThrows
    public ProjectStatistics getProjectStatistics(Long projectId) {
        Optional<Project> byId = projectRepository.findById(projectId);

        if (byId.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();
        Optional<ProjectMember> member = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }


        ProjectStatistics statistics = new ProjectStatistics();

        Long openTasks = taskRepository.countByProject_IdAndStatus_Name(projectId, "OPEN");
        Long closedTasks = taskRepository.countByProject_IdAndStatus_Name(projectId, "CLOSED");
        Long totalTasks = taskRepository.countByProject_Id(projectId);

        statistics.setOpenTasks(openTasks);
        statistics.setClosedTasks(closedTasks);
        statistics.setTotalTasks(totalTasks);

        /*LocalDate nextMonthDate = LocalDate.now().plusMonths(1);

        GregorianCalendar calendar = new GregorianCalendar();

        LocalDate dateTimeBoundary = nextMonthDate
                .withDayOfMonth(nextMonthDate.getMonth().length(calendar.isLeapYear(nextMonthDate.getYear())));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(now.toString()));*/


        return statistics;
    }
}
