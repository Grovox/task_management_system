package com.example.task.management.system.controller.impl;

import com.example.task.management.system.dto.*;
import com.example.task.management.system.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Контроллек обробатывающий запросы администратора для взаимодействия с задачами.
 *
 * @author Max Artremov
 */
@RequestMapping("admin/")
@RestController
@RequiredArgsConstructor
public class AdminTaskControllerImpl {

    private final TaskService taskService;

    @GetMapping(value = "/tasks")
    public Page<TaskResponse> getAll(@Min(value = 1) @RequestParam(defaultValue = "1") int page,
                                     @Min(value = 1)  @RequestParam(defaultValue = "10") int size) {
        return taskService.getAll(page, size);
    }

    @GetMapping(value = "/tasks-author")
    public Page<TaskResponse> getAllByAuthor(@NotNull @RequestParam UUID authorId,
                                             @Min(value = 1) @RequestParam(defaultValue = "1") int page,
                                             @Min(value = 1) @RequestParam(defaultValue = "10") int size) {
        return taskService.getAllByAuthor(authorId, page, size);
    }

    @GetMapping(value = "/tasks-performer")
    public Page<TaskResponse> getAllByPerformer(@NotNull @RequestParam UUID performerId,
                                                @Min(value = 1) @RequestParam(defaultValue = "1") int page,
                                                @Min(value = 1) @RequestParam(defaultValue = "10") int size) {
        return taskService.getAllByPerformer(performerId, page, size);
    }

    @PostMapping(value = "/tasks")
    public void create(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        taskService.create(createTaskRequest);
    }

    @PutMapping(value = "/tasks")
    public void change(@Valid @RequestBody ChangeTaskRequest changeTaskRequest) {
        taskService.change(changeTaskRequest);
    }

    @DeleteMapping(value = "/tasks")
    public void delete(@Valid @RequestBody DeleteTaskRequest deleteTaskRequest) {
        taskService.delete(deleteTaskRequest);
    }

    @PutMapping(value = "/task-status")
    public void changeStatus(@Valid @RequestBody ChangeTaskStatusRequest changeTaskStatusRequest) {
        taskService.changeStatus(changeTaskStatusRequest);
    }

    @PutMapping(value = "/task-priority")
    public void changePriority(@Valid @RequestBody ChangeTaskPriorityRequest changeTaskPriorityRequest) {
        taskService.changePriority(changeTaskPriorityRequest);
    }

    @PostMapping(value = "/task-comment")
    public void commentCreate(@Valid @RequestBody CreateCommentRequest createCommentRequest) {
        taskService.commentCreate(createCommentRequest);
    }

    @PostMapping(value = "/task-performer")
    public void setPerformer(@Valid @RequestBody SetTaskPerformerRequest setTaskPerformerRequest) {
        taskService.setPerformer(setTaskPerformerRequest);
    }

}
