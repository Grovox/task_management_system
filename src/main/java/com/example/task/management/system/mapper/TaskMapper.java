package com.example.task.management.system.mapper;

import com.example.task.management.system.dto.CreateTaskRequest;
import com.example.task.management.system.dto.TaskResponse;
import com.example.task.management.system.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Маппер для преобразования сущности задач.
 *
 * @author Max Artremov
 */
@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskResponse taskToTaskResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "performer", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Task createTaskRequestToTask(CreateTaskRequest createTaskRequest);
}
