package com.example.task.management.system.exception;

/**
 * Исключение, если истекло время жизни токена.
 *
 * @author Max Artremov
 */

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException(String message) {
        super(message);
    }
}
