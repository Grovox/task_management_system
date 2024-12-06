package com.example.task.management.system.repository;

import com.example.task.management.system.entity.Comment;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class CommentRepositoryIntegrationTest {

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
    private CommentRepository commentRepository;

    @AfterEach
    public void clearDB(){
        commentRepository.deleteAll();
    }

    @Test
    public void testSaveComment() {
        Comment comment = new Comment();

        commentRepository.save(comment);

        assertTrue(commentRepository.findById(comment.getId()).isPresent());
    }

    @Test
    public void testFindByIdComment() {
        Comment comment = new Comment();
        commentRepository.save(comment);

        Optional<Comment> testComment = commentRepository.findById(comment.getId());

        assertTrue(testComment.isPresent());
    }

    @Test
    public void testDeleteComment() {
        Comment comment = new Comment();
        commentRepository.save(comment);

        commentRepository.delete(comment);

        assertFalse(commentRepository.findById(comment.getId()).isPresent());
    }
}
