package com.example.task.management.system.service;

import com.example.task.management.system.entity.RefreshToken;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с refresh токеном.
 *
 * @author Max Artremov
 */
@Service

public class RefreshTokenService extends TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(@Value("${jwt.secret.refresh}") String secretKey,
                               @Value("${jwt.secret.refresh-expiration}") long secretKeyExpiration,
                               RefreshTokenRepository refreshTokenRepository) {
        super(secretKey, secretKeyExpiration);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Метод для сохранения токена.
     */
    public void saveToken(RefreshToken token){
        refreshTokenRepository.save(token);
    }

    /**
     * Метод для получения токена.
     */
    public RefreshToken getTokenByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }
}
