package com.example.task.management.system.exception;

/**
 * Исключения нарушения прав достума.
 *
 * @author Max Artremov
 */
public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}
