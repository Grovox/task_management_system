package com.example.task.management.system.dto;

import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Status;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO запроса на создание задачи.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {
    /**
     * Заголовок.
     */
    @Size(max = 255, message = "Заголовок должен содержать до 255 символов")
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
    @Size(max = 65535, message = "Описание должен содержать до 255 символов")
    private String description;
}
