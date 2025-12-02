package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta de autenticación exitosa")
public class AuthResponse {

    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String tipo = "Bearer";

    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Email del usuario", example = "juan@email.com")
    private String email;

    @Schema(description = "Rol del usuario", example = "ROLE_CLIENTE")
    private com.pasteleriamilsabores.backend.model.enums.RolUsuario rol;

    public AuthResponse(String token, Long id, String nombre, String email,
            com.pasteleriamilsabores.backend.model.enums.RolUsuario rol) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }
}
