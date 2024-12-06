package com.example.task.management.system.exception;

/**
 * Исключения, если сущность не найдена.
 *
 * @author Max Artremov
 */

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
