package com.example.task.management.system.dto;

import lombok.*;


import java.util.UUID;

/**
 * DTO описывающие общедоступные данные пользователя.
 *
 * @author Max Artremov
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    /**
     * id.
     */
    private UUID id;

    /**
     * Почта.
     */
    private String email;

    /**
     * Имя.
     */
    private String firstname;

    /**
     * Фамилия.
     */
    private String lastname;
}
