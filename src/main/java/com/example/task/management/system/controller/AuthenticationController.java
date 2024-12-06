package com.example.task.management.system.controller;

import com.example.task.management.system.dto.AuthenticationRequest;
import com.example.task.management.system.dto.RefreshTokenRequest;
import com.example.task.management.system.dto.TokenResponse;

/**
 * API контроллера аутентификации.
 *
 * @author Max Artremov
 */
public interface AuthenticationController {

    /**
     * Метод запроса аунтефикации.
     *
     * @param authenticationRequest данные запроса.
     * @return Токены доступа.
     */
    TokenResponse authenticate(AuthenticationRequest authenticationRequest);

    /**
     * Запрос обновления токенов доступа.
     * @param refreshTokenRequest данные запроса хранящие Refresh токен.
     * @return новые токены доступа.
     */
    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);


}
