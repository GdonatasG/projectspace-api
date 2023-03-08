package com.projectspace.projectspaceapi.taskpriority.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBodyList;
import com.projectspace.projectspaceapi.taskpriority.service.TaskPriorityService;
import com.projectspace.projectspaceapi.taskpriority.model.TaskPriority;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.TASK_PRIORITY_URL)
@RequiredArgsConstructor
public class TaskPriorityController {

    private final TaskPriorityService taskPriorityService;

    @GetMapping
    public ResponseEntity<SuccessBodyList<TaskPriority>> getTaskPriorities() {
        List<TaskPriority> priorities = taskPriorityService.getTaskPriorities();

        return new ResponseEntity<>(new SuccessBodyList<>(priorities), HttpStatus.OK);
    }


}
