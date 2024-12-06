package com.example.task.management.system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO запрос обновления токена.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {

    /**
     * Токен обновления.
     */
    @NotNull(message = "Токен не может быть пустым")
    private String refreshToken;
}
