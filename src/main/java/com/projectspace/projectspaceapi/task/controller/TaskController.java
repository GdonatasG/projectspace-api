package com.projectspace.projectspaceapi.task.controller;

import com.projectspace.projectspaceapi.authentication.AuthenticationConfigConstants;
import com.projectspace.projectspaceapi.common.response.SuccessBody;
import com.projectspace.projectspaceapi.common.response.SuccessBodyList;
import com.projectspace.projectspaceapi.task.request.CreateTaskRequest;
import com.projectspace.projectspaceapi.task.request.UpdateTaskRequest;
import com.projectspace.projectspaceapi.task.service.TaskService;
import com.projectspace.projectspaceapi.task.model.Task;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/assigned")
    public ResponseEntity<SuccessBodyList<Task>> getUserAssignedTasks(@RequestParam(name = "project_id") int projectId) {
        List<Task> tasks = taskService.getUserAssignedTasks(Integer.toUnsignedLong(projectId));

        return new ResponseEntity<>(new SuccessBodyList<>(tasks), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SuccessBody> createTask(@RequestBody @Valid CreateTaskRequest createTaskRequest) {
        taskService.createTask(createTaskRequest);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<SuccessBody> closeTask(@PathVariable int id) {
        taskService.changeTaskStatus(Integer.toUnsignedLong(id), true);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }

    @PutMapping("/{id}/open")
    public ResponseEntity<SuccessBody> openTask(@PathVariable int id) {
        taskService.changeTaskStatus(Integer.toUnsignedLong(id), false);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuccessBody> updateTask(@PathVariable int id, @RequestBody @Valid UpdateTaskRequest updateTaskRequest) {
        taskService.updateTask(Integer.toUnsignedLong(id), updateTaskRequest);

        return new ResponseEntity<>(new SuccessBody<>(), HttpStatus.OK);
    }
}
