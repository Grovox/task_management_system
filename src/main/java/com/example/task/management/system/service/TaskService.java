package com.example.task.management.system.service;

import com.example.task.management.system.dto.*;
import com.example.task.management.system.entity.Comment;
import com.example.task.management.system.entity.Task;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Role;
import com.example.task.management.system.entity.enums.Status;
import com.example.task.management.system.exception.EntityNotFoundException;
import com.example.task.management.system.exception.ForbiddenException;
import com.example.task.management.system.mapper.TaskMapper;
import com.example.task.management.system.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с задачами.
 *
 * @author Max Artremov
 */
@Service
@RequiredArgsConstructor
public class TaskService {
    /**
     * Репозиторий для работы с сущьностями задач.
     */
    private final TaskRepository taskRepository;

    /**
     * Сервис для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Сервис для работы с комментариями.
     */
    private final CommentService commentService;

    /**
     * Маппер для сущности задачи.
     */
    private final TaskMapper taskMapper;

    /**
     * Ищет задачу по id, если задача не найден, выбрасывает исключение.
     * @throws EntityNotFoundException исключение, сущность не найдена.
     */
    public Task getTaskById(UUID id) throws EntityNotFoundException {
        return taskRepository.findById(id).orElseThrow(() -> new  EntityNotFoundException("Задача не найден"));
    }

    /**
     * Метод потучения всех задач с пагинацией.
     */
    public Page<TaskResponse> getAll(int page, int size) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        List<Task> tasks;
        if (user.getRole().equals(Role.USER)){
            tasks = taskRepository.findByPerformer(user);
        } else tasks = taskRepository.findAll();
        return tasksToResponse(page, size, tasks);
    }

    /**
     * Метод потучения всех задач автора с пагинацией.
     */
    public Page<TaskResponse> getAllByAuthor(UUID authorId, int page, int size) {
        User author;
        try {
            author = userService.getUserById(authorId);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Автор не найден");
        }
        List<Task> tasks = taskRepository.findByAuthor(author);
        return tasksToResponse(page, size, tasks);
    }

    /**
     * Метод получения всех задач исполнителя с пагинацией.
     */
    public Page<TaskResponse> getAllByPerformer(UUID performerId, int page, int size) {
        User performer;
        try {
            performer = userService.getUserById(performerId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Исполнитель не найден");
        }
        List<Task> tasks = taskRepository.findByPerformer(performer);
        return tasksToResponse(page, size, tasks);
    }

    /**
     * Метод создания задачи.
     */
    public void create(CreateTaskRequest createTaskRequest) {
        Task task = taskMapper.createTaskRequestToTask(createTaskRequest);
        if (task.getStatus() == null) task.setStatus(Status.PENDING);
        if (task.getPriority() == null) task.setPriority(Priority.MEDIUM);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User author = userService.getUserByEmail(userDetails.getUsername());
        task.setAuthor(author);
        taskRepository.save(task);
    }

    /**
     * Метод изменения задачи.
     */
    public void change(ChangeTaskRequest changeTaskRequest) {
        Task task = getTaskById(changeTaskRequest.getId());
        User performer = null;
        if (changeTaskRequest.getPerformerId() != null){
            try {
                performer = userService.getUserById(changeTaskRequest.getPerformerId());
            } catch (Exception e) {
                throw new EntityNotFoundException("Исполнитель не найден");
            }
        }
        task.setTitle(changeTaskRequest.getTitle());
        task.setStatus(changeTaskRequest.getStatus());
        task.setPriority(changeTaskRequest.getPriority());
        task.setDescription(changeTaskRequest.getDescription());
        task.setPerformer(performer);
        taskRepository.save(task);
    }

    /**
     * Метод удаления задачи.
     */
    public void delete(DeleteTaskRequest deleteTaskRequest) {
        Task task = getTaskById(deleteTaskRequest.getId());
        taskRepository.delete(task);
    }

    /**
     * Метод изменения статуса задачи.
     */
    public void changeStatus(ChangeTaskStatusRequest changeTaskStatusRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        Task task = getTaskById(changeTaskStatusRequest.getId());
        if (user.getRole().equals(Role.USER) && !task.getPerformer().getId().equals(user.getId())){
            throw new ForbiddenException("Пользователь не является исполнителем задачи");
        }
        task.setStatus(changeTaskStatusRequest.getStatus());
        taskRepository.save(task);
    }

    /**
     * Метод изменения приоритета задачи.
     */
    public void changePriority(ChangeTaskPriorityRequest changeTaskPriorityRequest) {
        Task task = getTaskById(changeTaskPriorityRequest.getId());
        task.setPriority(changeTaskPriorityRequest.getPriority());
        taskRepository.save(task);
    }

    /**
     * Метод добавления комментария.
     */
    public void commentCreate(CreateCommentRequest createCommentRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUserByEmail(userDetails.getUsername());
        Task task = getTaskById(createCommentRequest.getTaskId());
        if (user.getRole().equals(Role.USER) && !task.getPerformer().getId().equals(user.getId())){
            throw new ForbiddenException("Пользователь не является исполнителем задачи");
        }
        commentService.createToTask(createCommentRequest, task);
    }

    /**
     * Метод назначения исполнителя.
     */
    public void setPerformer(SetTaskPerformerRequest setTaskPerformerRequest) {
        Task task = getTaskById(setTaskPerformerRequest.getTaskId());
        User performer = userService.getUserById(setTaskPerformerRequest.getPerformerId());
        task.setPerformer(performer);
        taskRepository.save(task);
    }

    /**
     * Метод пагинации списка задач.
     */
    private Page<TaskResponse> tasksToResponse(int page, int size, List<Task> tasks){
        int start = (page - 1) * size;
        if (start < 0) start = 0;
        int end = start + size;
        if (end > tasks.size()) end = tasks.size();
        List<TaskResponse> responses = new ArrayList<>();
        if (start < tasks.size()){
            responses = tasks.subList(start, end).stream().map(taskMapper::taskToTaskResponse).toList();
        }

        return new PageImpl<>(responses, PageRequest.of(page, size), responses.size());
    }
}
