package com.example.task.management.system.dto;

import lombok.*;

/**
 * DTO ответа в случае ошибки.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    /**
     * Сообщение об ошибки.
     */
    private String message;
}
