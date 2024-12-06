package com.example.task.management.system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


/**
 * DTO запроса на аутентификацию.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    /**
     * Почта.
     */
    @NotNull(message = "Email не может быть пустым")
    @Size(min = 6, max = 255, message = "Email должен содержать от 6 до 255 символов")
    private String email;

    /**
     * Пароль.
     */
    @NotNull(message = "Пароль не может быть пустым")
    @Size(min = 5, max = 255, message = "Пароль должен содержать от 6 до 255 символов")
    private String password;
}
