package com.example.task.management.system.dto;

import lombok.*;

import java.util.UUID;

/**
 * DTO описывает общие дланные коменнтария.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    /**
     * id.
     */
    private UUID id;

    /**
     * Текст комментария.
     */
    private String description;

    /**
     * Автор комментария.
     */
    private UserDTO author;
}
