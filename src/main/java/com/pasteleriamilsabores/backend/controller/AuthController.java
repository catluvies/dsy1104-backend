package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.AuthResponse;
import com.pasteleriamilsabores.backend.dto.LoginRequest;
import com.pasteleriamilsabores.backend.dto.RegisterRequest;
import com.pasteleriamilsabores.backend.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(
            @RequestHeader("X-Admin-Secret") String secret,
            @Valid @RequestBody RegisterRequest request) {

        if (!"MilSaboresAdmin2024".equals(secret)) {
            return ResponseEntity.status(403).body("Acceso denegado: Clave de administrador incorrecta");
        }

        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/register/vendedor")
    public ResponseEntity<AuthResponse> registerVendedor(@Valid @RequestBody RegisterRequest request) {
        // La seguridad de este endpoint (solo ADMIN) se maneja en SecurityConfig
        return ResponseEntity.ok(authService.registerVendedor(request));
    }
}
