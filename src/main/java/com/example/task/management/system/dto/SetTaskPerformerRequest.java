package com.example.task.management.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * DTO описывающие добавления исполнителя задачи.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetTaskPerformerRequest {
    /**
     * Id задачи.
     */
    @NotNull(message = "Id не может быть пуст")
    private UUID taskId;

    /**
     * Id исполнитель задачи.
     */
    @NotNull(message = "Id исполнителя не может быть пустым")
    private UUID performerId;
}
