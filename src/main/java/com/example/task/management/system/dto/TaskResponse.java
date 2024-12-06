package com.example.task.management.system.dto;

import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Status;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * DTO ответа на запрос задч.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {
    /**
     * id.
     */
    private UUID id;

    /**
     * Заголовок.
     */
    private String title;

    /**
     * Статус задачи.
     */
    private Status status;

    /**
     * Приоритет задачи.
     */
     private Priority priority;

    /**
     * Описание задачи.
     */
    private String description;

    /**
     * Автор задачи.
     */
    private UserDTO author;

    /**
     * Исполнитель задачи.
     */
    private UserDTO performer;

    /**
     * Коментарии задачи.
     */
    private List<CommentDTO> comments;
}
