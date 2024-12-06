package com.example.task.management.system.repository;

import com.example.task.management.system.entity.User;
import com.example.task.management.system.entity.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class UserRepositoryIntegrationTest {

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
    private UserRepository userRepository;

    @AfterEach
    public void clearDB(){
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
    public void testSaveUser() {
        User user = getUser();

        userRepository.save(user);

        assertTrue(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void testFindByIdUser() {
        User user = getUser();
        userRepository.save(user);

        Optional<User> testUser = userRepository.findById(user.getId());

        assertTrue(testUser.isPresent());
    }

    @Test
    public void testDeleteUser() {
        User user = getUser();
        userRepository.save(user);

        userRepository.delete(user);

        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void testFindByEmailUser() {
        User user = getUser();
        userRepository.save(user);

        Optional<User> testUser = userRepository.findByEmail(user.getEmail());

        assertTrue(testUser.isPresent());
    }
}
