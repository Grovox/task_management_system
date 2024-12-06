package com.example.task.management.system.controller.impl;

import com.example.task.management.system.dto.*;
import com.example.task.management.system.entity.Comment;
import com.example.task.management.system.entity.Task;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.entity.enums.Priority;
import com.example.task.management.system.entity.enums.Role;
import com.example.task.management.system.entity.enums.Status;
import com.example.task.management.system.repository.CommentRepository;
import com.example.task.management.system.repository.RefreshTokenRepository;
import com.example.task.management.system.repository.TaskRepository;
import com.example.task.management.system.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AdminTaskControllerImplIntegrationTest {

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

    @AfterEach
    public void clearDB() {
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    private User getUser() {
        return User.builder()
                .email("admin1@example.com")
                .password("$2a$10$zujHoO8RwhJl5miQsccht.sCQUCXELmtrJQn0ATL.gVpOWxYQEPde") // зашифрованных adminpassword
                .role(Role.ADMIN)
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
        String response = result.getResponse().getContentAsString();
        int s1 = response.indexOf("accessToken\":\"") + 14, e1 = response.indexOf("\"", s1);
        int s2 = response.indexOf("refreshToken\":\"") + 15, e2 = response.indexOf("\"", s2);
        return TokenResponse.builder().accessToken(response.substring(s1, e1)).refreshToken(response.substring(s2, e2)).build();
    }

    @Test
    void getAll() throws Exception {
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task1 = getTask(user);
        Task task2 = getTask(user);
        taskRepository.save(task1);
        taskRepository.save(task2);
        TokenResponse tokenResponse = authenticate();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/tasks")
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
    void getAllByAuthor() throws Exception {
        User user1 = getUser();
        user1.setEmail("qwerty1");
        User user2 = getUser();
        user2.setEmail("qwerty2");
        userRepository.save(user1);
        userRepository.save(user2);
        Task task1 = getTask(user1);
        Task task2 = getTask(user1);
        Task task3 = getTask(user2);
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
        TokenResponse tokenResponse = authenticate();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/tasks-author?authorId=" + user1.getId().toString())
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
    void getAllByPerformer() throws Exception {
        User user1 = getUser();
        user1.setEmail("qwerty1");
        User user2 = getUser();
        user2.setEmail("qwerty2");
        userRepository.save(user1);
        userRepository.save(user2);
        Task task1 = getTask(user1);
        task1.setPerformer(user1);
        Task task2 = getTask(user1);
        task2.setPerformer(user1);
        Task task3 = getTask(user2);
        task3.setPerformer(user2);
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);
        TokenResponse tokenResponse = authenticate();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/admin/tasks-performer?performerId=" + user1.getId().toString())
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
    void create() throws Exception {
        TokenResponse tokenResponse = authenticate();
        CreateTaskRequest createTaskRequest = CreateTaskRequest.builder()
                .title("123")
                .status(Status.PENDING)
                .priority(Priority.MEDIUM)
                .description("123")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(createTaskRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/tasks")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        List<Task> tasks = taskRepository.findAll();
        assertEquals(tasks.size(), 1);
    }

    @Test
    void change() throws Exception {
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task = getTask(user);
        task.setTitle("");
        taskRepository.save(task);
        TokenResponse tokenResponse = authenticate();
        ChangeTaskRequest changeTaskRequest = ChangeTaskRequest.builder()
                .id(task.getId())
                .title("12345")
                .status(task.getStatus())
                .priority(task.getPriority())
                .description(task.getDescription())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(changeTaskRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/admin/tasks")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        Optional<Task> taskResult = taskRepository.findById(task.getId());
        assertEquals("12345", taskResult.get().getTitle());
    }

    @Test
    void delete() throws Exception {
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task = getTask(user);
        taskRepository.save(task);
        TokenResponse tokenResponse = authenticate();
        DeleteTaskRequest deleteTaskRequest = DeleteTaskRequest.builder().id(task.getId()).build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(deleteTaskRequest);

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/tasks")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        Optional<Task> taskResult = taskRepository.findById(task.getId());
        assertFalse(taskResult.isPresent());
    }

    @Test
    void changeStatus() throws Exception {
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task = getTask(user);
        task.setStatus(Status.PENDING);
        taskRepository.save(task);
        TokenResponse tokenResponse = authenticate();
        ChangeTaskStatusRequest changeTaskStatusRequest = ChangeTaskStatusRequest.builder()
                .id(task.getId())
                .status(Status.COMPLETED)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(changeTaskStatusRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/admin/task-status")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        Optional<Task> taskResult = taskRepository.findById(task.getId());
        assertEquals(Status.COMPLETED, taskResult.get().getStatus());
    }

    @Test
    void changePriority() throws Exception {
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task = getTask(user);
        task.setPriority(Priority.LOW);
        taskRepository.save(task);
        TokenResponse tokenResponse = authenticate();
        ChangeTaskPriorityRequest changeTaskPriorityRequest = ChangeTaskPriorityRequest.builder()
                .id(task.getId())
                .priority(Priority.HIGH)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(changeTaskPriorityRequest);

        mockMvc.perform(MockMvcRequestBuilders.put("/admin/task-priority")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        Optional<Task> taskResult = taskRepository.findById(task.getId());
        assertEquals(Priority.HIGH, taskResult.get().getPriority());
    }

    @Test
    void commentCreate() throws Exception {
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task = getTask(user);
        taskRepository.save(task);
        TokenResponse tokenResponse = authenticate();
        CreateCommentRequest createCommentRequest = CreateCommentRequest.builder()
                .taskId(task.getId())
                .description("")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(createCommentRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/task-comment")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    void setPerformer() throws Exception {
        User user = getUser();
        user.setEmail("qwerty");
        userRepository.save(user);
        Task task = getTask(user);
        taskRepository.save(task);
        TokenResponse tokenResponse = authenticate();
        SetTaskPerformerRequest setTaskPerformerRequest = SetTaskPerformerRequest.builder()
                .performerId(user.getId())
                .taskId(task.getId())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(setTaskPerformerRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/task-performer")
                        .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());

        Optional<Task> taskResult = taskRepository.findById(task.getId());
        assertEquals(user.getId(), taskResult.get().getPerformer().getId());
    }
}