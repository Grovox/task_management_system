package com.example.task.management.system.config;

import com.example.task.management.system.repository.UserRepository;
import com.example.task.management.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Конфигурации приложения.
 *
 * @author Max Artremov
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    /**
     * Репозиторий сотрудника.
     */
    private final UserRepository userRepository;

    /**
     * Бин раздающий реализацию UserDetailsService которай ищет зотрудника по имени в бд.
     * @return реализацию UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь не найден."));
    }

    /**
     * Бин раздающий реализацию AuthenticationManager, для создания использует конфигурацию аутентификации.
     * @param configuration Конфигурации аутентификации.
     * @return реализацию AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Бин раздающий реализацию PasswordEncoder который мы мы будем использовать.
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
