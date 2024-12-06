package com.example.task.management.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * DTO запроса удаления задачи.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteTaskRequest {
    /**
     * id.
     */
    @NotNull(message = "Id не может быть пуст")
    private UUID id;
}
