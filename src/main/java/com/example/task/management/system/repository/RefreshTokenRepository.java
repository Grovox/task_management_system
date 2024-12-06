package com.example.task.management.system.repository;

import com.example.task.management.system.entity.RefreshToken;
import com.example.task.management.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для работы с сущностью токена.
 *
 * @author Max Artremov
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Поиск токена по пользователю.
     */
    RefreshToken findByUser(User user);
}
