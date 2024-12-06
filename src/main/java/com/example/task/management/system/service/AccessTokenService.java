package com.example.task.management.system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис для работы с access токеном.
 *
 * @author Max Artremov
 */
@Service
public class AccessTokenService extends TokenService {

    public AccessTokenService(@Value("${jwt.secret.access}") String secretKey,
                              @Value("${jwt.secret.access-expiration}") long secretKeyExpiration) {
        super(secretKey, secretKeyExpiration);
    }
}
