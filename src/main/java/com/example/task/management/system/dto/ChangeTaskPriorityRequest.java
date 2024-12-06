package com.example.task.management.system.dto;

import com.example.task.management.system.entity.enums.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * DTO описывающее запрос на изменение приоритета задачи.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTaskPriorityRequest {
    /**
     * id.
     */
    @NotNull(message = "Id не может быть пуст")
    private UUID id;

    /**
     * Приоритет задачи.
     */
    @NotNull(message = "Приоритет не может быть пуст")
    private Priority priority;
}
