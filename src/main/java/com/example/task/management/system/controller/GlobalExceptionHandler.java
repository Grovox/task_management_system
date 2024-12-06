package com.example.task.management.system.controller;

import com.example.task.management.system.dto.ErrorResponse;
import com.example.task.management.system.exception.EntityNotFoundException;
import com.example.task.management.system.exception.ExpiredTokenException;
import com.example.task.management.system.exception.ForbiddenException;
import com.example.task.management.system.exception.InvalidTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик ошибок на уровне контроллера.
 *
 * @author Max Artremov
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка исключений прав доступа.
     */
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenExceptions(ForbiddenException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    /**
     * Обработка исключений авторизации.
     */
    @ExceptionHandler({BadCredentialsException.class, InvalidTokenException.class, ExpiredTokenException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorizedExceptions(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Обработка исключений неправельно переданных данных.
     */
    @ExceptionHandler({EntityNotFoundException.class, MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleBedRequestException(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }



    /**
     * Обработка исключений невалидных данных.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Обработка прочих исключений
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.I_AM_A_TEAPOT);
    }
}
