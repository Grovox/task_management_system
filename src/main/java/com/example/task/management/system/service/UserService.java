package com.example.task.management.system.service;

import com.example.task.management.system.entity.User;
import com.example.task.management.system.exception.EntityNotFoundException;
import com.example.task.management.system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис для работы с пользователями.
 *
 * @author Max Artremov
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Ищет пользователя по почте, если пользователь не найден, выбрасывает исключение.
     * @throws EntityNotFoundException исключение, сущность не найдена.
     */
    public User getUserByEmail(String email) throws EntityNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new  EntityNotFoundException("Пользователь не найден"));
    }

    /**
     * Ищет пользователя по id, если пользователь не найден, выбрасывает исключение.
     * @throws EntityNotFoundException исключение, сущность не найдена.
     */
    public User getUserById(UUID id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new  EntityNotFoundException("Пользователь не найден"));
    }
}
