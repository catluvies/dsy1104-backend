package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Datos para iniciar sesión")
public class LoginRequest {

    @Schema(description = "Correo electrónico", example = "cliente@milsabores.cl")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @Schema(description = "Contraseña", example = "cliente123")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
