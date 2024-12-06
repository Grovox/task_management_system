package com.example.task.management.system.mapper;

import com.example.task.management.system.dto.CreateCommentRequest;
import com.example.task.management.system.dto.CreateTaskRequest;
import com.example.task.management.system.entity.Comment;
import com.example.task.management.system.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования сущности комментария.
 *
 * @author Max Artremov
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment createCommentRequestToComment(CreateCommentRequest createCommentRequest);
}
