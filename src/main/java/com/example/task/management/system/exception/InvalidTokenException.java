package com.example.task.management.system.exception;

/**
 * Неправельный токен доступа.
 *
 * @author Max Artremov
 */
public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }
}
