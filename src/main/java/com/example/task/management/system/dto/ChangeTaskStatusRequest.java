package com.example.task.management.system.dto;

import com.example.task.management.system.entity.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

/**
 * DTO для изменения статуса задачи.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTaskStatusRequest {
    /**
     * id.
     */
    @NotNull(message = "Id не может быть пуст")
    private UUID id;

    /**
     * Статус задачи.
     */
    @NotNull(message = "Статус не может быть пуст")
    private Status status;

}
