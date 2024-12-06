package com.example.task.management.system.service;

import com.example.task.management.system.exception.ExpiredTokenException;
import com.example.task.management.system.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Прототип сервиса для работы с jwt токенами.
 *
 * @author Max Artremov
 */
@RequiredArgsConstructor
public abstract class TokenService {
    /**
     * Секретный ключ для токена.
     */
    private final String secretKey;

    /**
     * Время жизни токена.
     */
    private final long secretKeyExpiration;

    /**
     * Возвращяет имя пользователя токена.
     * @throws InvalidTokenException ошибка работы с токеном.
     */
    public String extractUsername(String token) throws InvalidTokenException {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Возврощяет данный токена по методу.
     * @param token Токен.
     * @param claimResolver Метод для получения данных.
     * @return Данные токена.
     * @param <T> Тип возврощяеммых данных.
     * @throws InvalidTokenException ошибка работы с токеном.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) throws InvalidTokenException {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Создает токен для пользователя.
     */
    public String generateToken(UserDetails userDetails){
        return Jwts
                .builder()
                .claims(new HashMap<>())
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + secretKeyExpiration * 60 * 1000))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Проверяет валидность токена, если не валиден, выбрасывает есключение.
     * @param token       токен для валидации.
     * @param userDetails предпологаемый владелец токена.
     * @throws InvalidTokenException ошибка работы с токеном.
     * @throws ExpiredTokenException истекло время жизни токена.
     */
    public void tokenValidation(String token, UserDetails userDetails) throws InvalidTokenException, ExpiredTokenException {
        if (token == null || token.isEmpty()) throw new InvalidTokenException("Токен недействителен");
        final String username = extractUsername(token);
        if ((!username.equals(userDetails.getUsername()))) throw new InvalidTokenException("Токен недействителен");
        isTokenExpired(token);
    }

    /**
     * Проверяет не истекло ли время жизни токена, если истекло, выбрасывает исключение.
     * @throws InvalidTokenException ошибка работы с токеном.
     * @throws ExpiredTokenException истекло время жизни токена.
     */
    private void isTokenExpired(String token) throws ExpiredTokenException, InvalidTokenException {
        if (extractExpiration(token).before(new Date())) throw new ExpiredTokenException("Время жизни токена истекло");
    }

    /**
     * Возврощяет срок жизни токена.
     * @throws InvalidTokenException ошибка работы с токеном.
     */
    private Date extractExpiration(String token) throws InvalidTokenException {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Возвращяет все данные токена, если появляется ошибка при получении данных пользователя, выбрасывает исключения.
     * @throws InvalidTokenException ошибка работы с токеном.
     */
    private Claims extractAllClaims(String token) throws InvalidTokenException {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e){
            throw new InvalidTokenException("Токен недействителен");
        }
    }

    /**
     * Декадирует и возвращяет секретный ключ.
     */
    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
