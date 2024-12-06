package com.example.task.management.system.controller.impl;

import com.example.task.management.system.dto.ChangeTaskStatusRequest;
import com.example.task.management.system.dto.CreateCommentRequest;
import com.example.task.management.system.dto.TaskResponse;
import com.example.task.management.system.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер запросов пользователя для взаимодействия с задачами.
 *
 * @author Max Artremov
 */
@RequestMapping("user/")
@RestController
@RequiredArgsConstructor
public class UserTaskControllerImpl {

    private final TaskService taskService;

    @GetMapping(value = "/tasks")
    public Page<TaskResponse> getAll(@Min(value = 1) @RequestParam(defaultValue = "1") int page,
                                     @Min(value = 1) @RequestParam(defaultValue = "10") int size){
       return taskService.getAll(page, size);
    }

    @PostMapping(value = "/task-comment")
    public void commentCreate(@Valid @RequestBody CreateCommentRequest createCommentRequest){
        taskService.commentCreate(createCommentRequest);
    }

    @PutMapping(value = "/task-status")
    public void changeStatus(@Valid @RequestBody ChangeTaskStatusRequest changeTaskStatusRequest){
        taskService.changeStatus(changeTaskStatusRequest);
    }

}
