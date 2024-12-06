package com.example.task.management.system.controller.impl;

import com.example.task.management.system.controller.AuthenticationController;
import com.example.task.management.system.dto.AuthenticationRequest;
import com.example.task.management.system.dto.RefreshTokenRequest;
import com.example.task.management.system.dto.TokenResponse;
import com.example.task.management.system.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер аутентификации.
 *
 * @author Max Artremov
 */
@RequestMapping("auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationControllerImpl implements AuthenticationController {

    private final AuthenticationService authenticationService;

    @Override
    @PostMapping(value = "/authenticate")
    public TokenResponse authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        return authenticationService.authenticate(authenticationRequest);
    }

    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }
}
