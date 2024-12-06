package com.example.task.management.system.service;

import com.example.task.management.system.dto.AuthenticationRequest;
import com.example.task.management.system.dto.RefreshTokenRequest;
import com.example.task.management.system.dto.TokenResponse;
import com.example.task.management.system.entity.RefreshToken;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.exception.EntityNotFoundException;
import com.example.task.management.system.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Сервис отвечающий за аутентификацию.
 *
 * @author Max Artremov
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    /**
     * Сервис даботы с пользователями.
     */
    private final UserService userService;

    /**
     * Сервис для работы с access токенами.
     */
    private final AccessTokenService accessJwtService;

    /**
     * Сервис для работы с refresh токенами.
     */
    private final RefreshTokenService refreshTokenService;

    /**
     * Менеджер аутентификации.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Метод для авторизации пользователя и выдачи токена.
     *
     * @param request Данные для авторизации.
     * @return Токены.
     */
    public TokenResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = null;
        try {
            user = userService.getUserByEmail(request.getEmail());
        } catch (EntityNotFoundException e) {
            throw new InvalidTokenException(e.getMessage());
        }
        String accessToken = accessJwtService.generateToken(user);
        String refreshToken = refreshTokenService.generateToken(user);
        saveRefreshToken(refreshToken, user);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Метод обновления токенов.
     */
    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String token = refreshTokenRequest.getRefreshToken();
        String email = refreshTokenService.extractUsername(token);

        User user = null;
        try {
            user = userService.getUserByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new InvalidTokenException(e.getMessage());
        }
        refreshTokenService.tokenValidation(token, user);
        RefreshToken dbToken = refreshTokenService.getTokenByUser(user);
        if (dbToken == null || !dbToken.getToken().equals(token)) throw new InvalidTokenException("Токен не подтвержден.");
        String accessToken = accessJwtService.generateToken(user);
        String refreshToken = refreshTokenService.generateToken(user);
        saveRefreshToken(refreshToken, user);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Метод для сохранения refresh токена.
     */
    private void saveRefreshToken(String token, User user){
        RefreshToken refreshToken = refreshTokenService.getTokenByUser(user);
        if (refreshToken == null){
            refreshToken = RefreshToken
                    .builder()
                    .token(token)
                    .user(user)
                    .build();
        } else refreshToken.setToken(token);
        refreshTokenService.saveToken(refreshToken);
    }
}
