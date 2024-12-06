package com.example.task.management.system.service;

import com.example.task.management.system.dto.CreateCommentRequest;
import com.example.task.management.system.dto.CreateTaskRequest;
import com.example.task.management.system.entity.Comment;
import com.example.task.management.system.entity.Task;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Status;
import com.example.task.management.system.mapper.CommentMapper;
import com.example.task.management.system.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Сервис который описывает взаимодействие с сущностью.
 *
 * @author Max Artremov
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    /**
     * Репозиторий для работы с сущностью комментария.
     */
    private final CommentRepository commentRepository;

    /**
     * Маппер для преобразования с сущьностью комментария.
     */
    private final CommentMapper commentMapper;

    /**
     * Сервис для работы с пользователями.
     */
    private final UserService userService;

    /**
     * Метод для создания комментария.
     */
    public void createToTask(CreateCommentRequest createCommentRequest, Task task) {
        Comment comment = commentMapper.createCommentRequestToComment(createCommentRequest);
        comment.setTask(task);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User author = userService.getUserByEmail(userDetails.getUsername());
        comment.setAuthor(author);
        commentRepository.save(comment);
    }
}
