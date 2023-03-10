package com.projectspace.projectspaceapi.task.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.task.service.TaskService;
import com.projectspace.projectspaceapi.task.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AuthenticationConfigConstants.TASK_URL)
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<SuccessBody<Task>> getTask(@PathVariable int id) {
        Task task = taskService.getTask(Integer.toUnsignedLong(id));

        return new ResponseEntity<>(new SuccessBody<>(task), HttpStatus.OK);
    }
}
