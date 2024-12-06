package com.example.task.management.system.dto;

import lombok.*;

import java.util.UUID;

/**
 * DTO описывающее запрос на создания комментария.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    /**
     * Текст комментария.
     */
    private String description;

    /**
     * Id задачи для которой создается комментарий.
     */
    private UUID taskId;
}
