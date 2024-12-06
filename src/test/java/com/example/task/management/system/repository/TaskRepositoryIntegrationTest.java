package com.example.task.management.system.repository;

import com.example.task.management.system.entity.Task;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Role;
import com.example.task.management.system.entity.enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class TaskRepositoryIntegrationTest {

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
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clearDB(){
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    private User getUser(){
        return User.builder()
                .email("email@email")
                .password("123")
                .role(Role.USER)
                .build();
    }

    private Task getTask(User user){
        return Task.builder()
                .status(Status.PENDING)
                .priority(Priority.MEDIUM)
                .author(user)
                .build();
    }

    @Test
    public void testSaveTask() {
        User user = getUser();
        userRepository.save(user);
        Task task = getTask(user);

        taskRepository.save(task);

        assertTrue(taskRepository.findById(task.getId()).isPresent());
    }

    @Test
    public void testFindByIdTask() {
        User user = getUser();
        userRepository.save(user);
        Task task = getTask(user);
        taskRepository.save(task);

        Optional<Task> testTask = taskRepository.findById(task.getId());

        assertTrue(testTask.isPresent());
    }

    @Test
    public void testDeleteTask() {
        User user = getUser();
        userRepository.save(user);
        Task task = getTask(user);
        taskRepository.save(task);

        taskRepository.delete(task);

        assertFalse(taskRepository.findById(task.getId()).isPresent());
    }

    @Test
    public void testFindAllTask() {
        User user = getUser();
        userRepository.save(user);
        Task task1 = getTask(user);
        taskRepository.save(task1);
        Task task2 = getTask(user);
        taskRepository.save(task2);

        List<Task> tasks = taskRepository.findAll();

        assertEquals(2, tasks.size());
    }

    @Test
    public void testFindByAuthorTask() {
        User user1 = getUser();
        userRepository.save(user1);
        Task task1 = getTask(user1);
        taskRepository.save(task1);
        Task task2 = getTask(user1);
        taskRepository.save(task2);
        User user2 = getUser();
        user2.setEmail("qwerty");
        userRepository.save(user2);
        Task task3 = getTask(user2);
        taskRepository.save(task3);

        List<Task> tasks = taskRepository.findByAuthor(user1);

        assertEquals(2, tasks.size());
    }

    @Test
    public void testFindByPerformerTask() {
        User user1 = getUser();
        User user2 = getUser();
        user2.setEmail("qwerty");
        userRepository.save(user1);
        userRepository.save(user2);
        Task task1 = getTask(user1);
        task1.setPerformer(user2);
        taskRepository.save(task1);
        Task task2 = getTask(user1);
        task2.setPerformer(user2);
        taskRepository.save(task2);
        Task task3 = getTask(user2);
        task3.setPerformer(user1);
        taskRepository.save(task3);

        List<Task> tasks = taskRepository.findByPerformer(user1);

        assertEquals(1, tasks.size());
    }
}
