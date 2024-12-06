package com.example.task.management.system.repository;

import com.example.task.management.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозитория для работы с сущностю пользователя.
 *
 * @author Max Artremov
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Поиск пользователя по email.
     */
    Optional<User> findByEmail(String email);
}
