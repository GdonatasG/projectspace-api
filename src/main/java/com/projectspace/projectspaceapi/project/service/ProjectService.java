package com.projectspace.projectspaceapi.project.service;

import com.projectspace.projectspaceapi.common.exception.AlreadyTakenException;
import com.projectspace.projectspaceapi.common.exception.ForbiddenException;
import com.projectspace.projectspaceapi.common.exception.NotFoundException;
import com.projectspace.projectspaceapi.common.helpers.AuthenticationUserHelper;
import com.projectspace.projectspaceapi.invitation.repository.InvitationRepository;
import com.projectspace.projectspaceapi.project.model.Project;
import com.projectspace.projectspaceapi.project.model.ProjectRisk;
import com.projectspace.projectspaceapi.project.model.ProjectRiskLevel;
import com.projectspace.projectspaceapi.project.model.ProjectStatistics;
import com.projectspace.projectspaceapi.project.repository.ProjectRepository;
import com.projectspace.projectspaceapi.project.request.CreateProjectRequest;
import com.projectspace.projectspaceapi.project.request.DeleteProjectRequest;
import com.projectspace.projectspaceapi.project.request.UpdateProjectRequest;
import com.projectspace.projectspaceapi.projectmember.model.ProjectMember;
import com.projectspace.projectspaceapi.projectmemberlevel.model.ProjectMemberLevel;
import com.projectspace.projectspaceapi.projectmember.repository.ProjectMemberRepository;
import com.projectspace.projectspaceapi.task.repository.TaskRepository;
import com.projectspace.projectspaceapi.taskassignee.repository.TaskAssigneeRepository;
import com.projectspace.projectspaceapi.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Project> getUserAvailableProjects(Boolean owned) {
        Long userId = authenticationUserHelper.getCurrentUser().getId();

        if (owned == null) {
            return projectRepository.findAllByProjectMembers_UserId(userId);
        }

        if (owned) {
            return projectRepository.findAllByOwnerId(userId);
        }

        return projectRepository.findAllByProjectMembers_UserIdAndOwnerIdNot(userId, userId);
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
        Optional<Project> byId = projectRepository.findById(updateProjectRequest.getProject_id());

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

        return statistics;
    }

    public ProjectRisk getProjectRisk(Long projectId) {
        Optional<Project> byId = projectRepository.findById(projectId);

        if (byId.isEmpty()) {
            throw new NotFoundException("Project not found!");
        }

        User currentUser = authenticationUserHelper.getCurrentUser();
        Optional<ProjectMember> member = projectMemberRepository.findByProjectIdAndUserId(projectId, currentUser.getId());

        if (member.isEmpty()) {
            throw new ForbiddenException();
        }

        ProjectRisk projectRisk = new ProjectRisk();

        LocalDateTime localDateBoundary = LocalDateTime.now().plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);

        System.out.println(localDateBoundary);

        Long openTasks = taskRepository.countByProject_IdAndStatus_NameAndEndDateBefore(projectId, "OPEN", localDateBoundary);
        Long totalTasks = taskRepository.countByProject_IdAndEndDateBefore(projectId, localDateBoundary);

        projectRisk.setOpenTasks(openTasks);
        projectRisk.setTotalTasks(totalTasks);
        projectRisk.setRisk(calculateRiskLevel(openTasks, totalTasks));

        return projectRisk;
    }

    private String calculateRiskLevel(Long openTasks, Long totalTasks) {
        if (totalTasks == 0) {
            return ProjectRiskLevel.UNKNOWN.getVisualName();
        }

        if (openTasks == 0) {
            return ProjectRiskLevel.NO_RISK.getVisualName();
        }

        GregorianCalendar calendar = new GregorianCalendar();

        LocalDate date = LocalDate.now();
        boolean isLeapYear = calendar.isLeapYear(date.getYear());

        float monthlyRiskImpact = (float) date.getDayOfMonth() / date.getMonth().length(isLeapYear);

        if (openTasks.equals(totalTasks)) {
            return getRiskLevelByMonthlyImpactOnly(monthlyRiskImpact).getVisualName();
        }

        float tasksLeftInPercentage = (float) openTasks / totalTasks;
        float riskValue = tasksLeftInPercentage + monthlyRiskImpact;

        return getRiskLevelByTasksAndMonthlyImpacts(riskValue).getVisualName();
    }

    private ProjectRiskLevel getRiskLevelByMonthlyImpactOnly(float monthlyRiskImpact) {
        if (monthlyRiskImpact < 0.5) {
            return ProjectRiskLevel.LOW;
        }

        if (monthlyRiskImpact < 0.75) {
            return ProjectRiskLevel.MEDIUM;
        }

        return ProjectRiskLevel.HIGH;
    }

    private ProjectRiskLevel getRiskLevelByTasksAndMonthlyImpacts(float riskValue) {
        if (riskValue < 1) {
            return ProjectRiskLevel.LOW;
        }

        if (riskValue < 1.25) {
            return ProjectRiskLevel.MEDIUM;
        }

        return ProjectRiskLevel.HIGH;
    }
}
