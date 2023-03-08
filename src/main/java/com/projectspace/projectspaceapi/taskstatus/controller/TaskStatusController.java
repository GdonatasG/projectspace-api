package com.projectspace.projectspaceapi.taskstatus.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBodyList;
import com.projectspace.projectspaceapi.taskstatus.service.TaskStatusService;
import com.projectspace.projectspaceapi.taskstatus.model.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.TASK_STATUS_URL)
@RequiredArgsConstructor
public class TaskStatusController {
    private final TaskStatusService taskStatusService;

    @GetMapping
    public ResponseEntity<SuccessBodyList<TaskStatus>> getTaskStatuses() {
        List<TaskStatus> statuses = taskStatusService.getTaskStatuses();

        return new ResponseEntity<>(new SuccessBodyList<>(statuses), HttpStatus.OK);
    }

}
