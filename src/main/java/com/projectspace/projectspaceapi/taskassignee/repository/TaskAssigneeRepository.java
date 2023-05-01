package com.projectspace.projectspaceapi.taskassignee.repository;

import com.projectspace.projectspaceapi.taskassignee.model.TaskAssignee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssigneeRepository extends JpaRepository<TaskAssignee, Long> {
    void deleteAllByTaskId(Long taskId);

    void deleteAllByTask_Project_Id(Long projectId);

    void deleteAllByProjectMember_Id(Long memberId);
}
