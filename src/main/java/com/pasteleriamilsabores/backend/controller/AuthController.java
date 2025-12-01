package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.AuthResponse;
import com.pasteleriamilsabores.backend.dto.CambiarPasswordRequest;
import com.pasteleriamilsabores.backend.dto.LoginRequest;
import com.pasteleriamilsabores.backend.dto.RegisterRequest;
import com.pasteleriamilsabores.backend.security.UsuarioPrincipal;
import com.pasteleriamilsabores.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints de login y registro")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y retorna un token JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Registrar cliente", description = "Registra un nuevo usuario con rol CLIENTE")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Registrar vendedor", description = "Registra un nuevo usuario con rol VENDEDOR (solo ADMIN)")
    @PostMapping("/register/vendedor")
    public ResponseEntity<AuthResponse> registerVendedor(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerVendedor(request));
    }

    @Operation(summary = "Cambiar contraseña", description = "Usuario autenticado cambia su propia contraseña")
    @PostMapping("/cambiar-password")
    public ResponseEntity<Void> cambiarPassword(
            @Valid @RequestBody CambiarPasswordRequest request,
            Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        authService.cambiarPassword(principal.getId(), request);
        return ResponseEntity.ok().build();
    }
}
