package com.Jakima_Estate.controllers;

import com.Jakima_Estate.dtos.LoginDTO;
import com.Jakima_Estate.dtos.RegisterDTO;
import com.Jakima_Estate.dtos.ResponseDTO;
import com.Jakima_Estate.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication endpoints")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and get JWT token")
    public ResponseDTO authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        return authService.authenticateUser(loginDTO);
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user")
    public ResponseDTO registerUser(@Valid @RequestBody RegisterDTO registerDTO) {
        return authService.registerUser(registerDTO);
    }
}