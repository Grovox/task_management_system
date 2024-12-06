package com.example.task.management.system.controller.impl;

import com.example.task.management.system.dto.AuthenticationRequest;
import com.example.task.management.system.dto.RefreshTokenRequest;
import com.example.task.management.system.entity.User;
import com.example.task.management.system.entity.enums.Role;
import com.example.task.management.system.repository.RefreshTokenRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerImplIntegrationTest {

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

    @AfterEach
    public void clearDB() {
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

    @Test
    void authenticate() throws Exception {
        User user = getUser();
        userRepository.save(user);
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .email(user.getEmail())
                .password("adminpassword")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(authenticationRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void refreshToken() throws Exception {
        User user = getUser();
        userRepository.save(user);
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder().email(user.getEmail()).password("adminpassword").build();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(authenticationRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)).andReturn();
        String response = result.getResponse().getContentAsString();
        int s = response.indexOf("refreshToken\":\"") + 15, e = response.indexOf("\"", s);
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder().refreshToken(response.substring(s, e)).build();
        request = objectMapper.writeValueAsString(refreshTokenRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }
}