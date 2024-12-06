package com.example.task.management.system.dto;

import lombok.*;

/**
 * DTO для ответа по аутентификации.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    /**
     * Токен доступа.
     */
    private String accessToken;

    /**
     * Токен обновления.
     */
    private String refreshToken;
}
