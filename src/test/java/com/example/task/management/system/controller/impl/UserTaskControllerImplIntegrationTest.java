package com.example.task.management.system.controller.impl;

import com.example.task.management.system.dto.AuthenticationRequest;
import com.example.task.management.system.dto.ChangeTaskStatusRequest;
import com.example.task.management.system.dto.CreateCommentRequest;
import com.example.task.management.system.dto.TokenResponse;
import com.example.task.management.system.entity.Task;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Role;
import com.example.task.management.system.entity.enums.Status;
import com.example.task.management.system.repository.CommentRepository;
import com.example.task.management.system.repository.RefreshTokenRepository;
import com.example.task.management.system.repository.TaskRepository;
import com.example.task.management.system.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UserTaskControllerImplIntegrationTest {

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
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User userAuth = null;

    @AfterEach
    public void clearDB() {
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
        userAuth = null;
    }

    private User getUser() {
        return User.builder()
                .email("user1@example.com")
                .password("$2a$10$zujHoO8RwhJl5miQsccht.sCQUCXELmtrJQn0ATL.gVpOWxYQEPde") // зашифрованных userpassword
                .role(Role.USER)
                .build();
    }

    private Task getTask(User user) {
        return Task.builder()
                .status(Status.PENDING)
                .priority(Priority.MEDIUM)
                .author(user)
                .build();
    }

    public TokenResponse authenticate() throws Exception {
        User user = getUser();
        userRepository.save(user);
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().email(user.getEmail()).password("adminpassword").build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(authenticationRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn();
        userAuth = user;
        String response = result.getResponse().getContentAsString();
        int s1 = response.indexOf("accessToken\":\"") + 14, e1 = response.indexOf("\"", s1);
        int s2 = response.indexOf("refreshToken\":\"") + 15, e2 = response.indexOf("\"", s2);
        return TokenResponse.builder().accessToken(response.substring(s1, e1)).refreshToken(response.substring(s2, e2)).build();
    }

    @Test
    void getAll() throws Exception {
        TokenResponse tokenResponse = authenticate();
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task1 = getTask(user);
        task1.setPerformer(userAuth);
        Task task2 = getTask(user);
        task2.setPerformer(userAuth);
        Task task3 = getTask(user);
        task3.setPerformer(user);
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/tasks")
                .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        int count = 0;
        int index = 0;
        while ((index = response.indexOf("comments", index)) != -1) {
            count++;
            index += "comments".length();
        }

        assertEquals(2, count);
    }

    @Test
    void commentCreate() throws Exception {
        TokenResponse tokenResponse = authenticate();
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task = getTask(user);
        task.setPerformer(userAuth);
        taskRepository.save(task);
        CreateCommentRequest createCommentRequest = CreateCommentRequest.builder()
                .taskId(task.getId())
                .description("")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/task-comment")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    void changeStatus() throws Exception {
        TokenResponse tokenResponse = authenticate();
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task = getTask(user);
        task.setPerformer(userAuth);
        task.setStatus(Status.PENDING);
        taskRepository.save(task);
        ChangeTaskStatusRequest changeTaskStatusRequest = ChangeTaskStatusRequest.builder()
                .id(task.getId())
                .status(Status.COMPLETED)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(changeTaskStatusRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/task-status")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        Optional<Task> taskResult = taskRepository.findById(task.getId());
        assertEquals(Status.COMPLETED, taskResult.get().getStatus());
    }
}