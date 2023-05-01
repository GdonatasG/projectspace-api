package com.projectspace.projectspaceapi.task.repository;

import com.projectspace.projectspaceapi.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProject_IdAndAssignees_ProjectMember_Id(Long projectId, Long memberId);

    List<Task> findAllByProject_IdAndCreator_Id(Long projectId, Long memberId);

    List<Task> findAllByProject_Id(Long projectId);

    Long countByProject_IdAndStatus_Name(Long projectId, String statusName);

    Long countByProject_Id(Long projectId);

    Long countByProject_IdAndStatus_NameAndEndDateBefore(Long projectId, String statusName, LocalDateTime date);

    Long countByProject_IdAndEndDateBefore(Long projectId, LocalDateTime date);

    void deleteAllByProject_Id(Long projectId);
}
