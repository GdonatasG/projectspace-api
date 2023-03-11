package com.projectspace.projectspaceapi.task.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.task.request.CreateTaskRequest;
import com.projectspace.projectspaceapi.task.service.TaskService;
import com.projectspace.projectspaceapi.task.model.Task;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<SuccessBody> createTask(@RequestBody @Valid CreateTaskRequest createTaskRequest) {
        taskService.createTask(createTaskRequest);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }
}
