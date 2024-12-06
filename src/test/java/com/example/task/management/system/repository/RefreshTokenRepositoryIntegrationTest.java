package com.example.task.management.system.repository;

import com.example.task.management.system.entity.RefreshToken;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.entity.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
public class RefreshTokenRepositoryIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("task_management_system_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clearDB(){
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    private User getUser(){
        return User.builder()
                .email("email@email")
                .password("123")
                .role(Role.USER)
                .build();
    }

    @Test
    public void testSaveRefreshToken() {
        User user = getUser();
        userRepository.save(user);
        RefreshToken refreshToken = RefreshToken.builder().token("").user(user).build();

        refreshTokenRepository.save(refreshToken);

        assertTrue(refreshTokenRepository.findById(refreshToken.getId()).isPresent());
    }

    @Test
    public void testFindByIdRefreshToken() {
        User user = getUser();
        userRepository.save(user);
        RefreshToken refreshToken = RefreshToken.builder().token("").user(user).build();
        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> testRefreshToken = refreshTokenRepository.findById(refreshToken.getId());

        assertTrue(testRefreshToken.isPresent());
    }

    @Test
    public void testDeleteRefreshToken() {
        User user = getUser();
        userRepository.save(user);
        RefreshToken refreshToken = RefreshToken.builder().token("").user(user).build();
        refreshTokenRepository.save(refreshToken);

        refreshTokenRepository.delete(refreshToken);

        assertFalse(refreshTokenRepository.findById(refreshToken.getId()).isPresent());
    }

    @Test
    public void testFindByUserRefreshToken(){
        User user = getUser();
        userRepository.save(user);
        RefreshToken refreshToken = RefreshToken.builder().token("").user(user).build();
        refreshTokenRepository.save(refreshToken);

        RefreshToken refreshTokenTest = refreshTokenRepository.findByUser(user);

        assertNotNull(refreshTokenTest);
    }
}
