package com.example.task.management.system.dto;

import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * DTO описывающее запрос на изменение задачи.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTaskRequest {
    /**
     * Id.
     */
    @NotNull(message = "Id не может быть пуст")
    private UUID id;

    /**
     * Заголовок.
     */
    @Size(max = 255, message = "Заголовок должен содержать от 6 до 255 символов")
    private String title;

    /**
     * Статус задачи.
     */
    @NotNull(message = "Статус не может быть пуст")
    private Status status;

    /**
     * Приоритет задачи.
     */
    @NotNull(message = "Приоритет не может быть пуст")
    private Priority priority;

    /**
     * Описание задачи.
     */
    @Size(max = 65535, message = "Описание должен содержать от 6 до 255 символов")
    private String description;

    /**
     * Id исполнитель задачи.
     */
    private UUID performerId;
}
