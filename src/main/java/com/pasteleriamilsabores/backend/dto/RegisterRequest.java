package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para registrar un nuevo usuario")
public class RegisterRequest {

    @Schema(description = "Nombre", example = "Juan")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    @Schema(description = "Apellido", example = "Pérez")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder 50 caracteres")
    private String apellido;

    @Schema(description = "Correo electrónico", example = "juan@email.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @Schema(description = "Contraseña (mínimo 6 caracteres)", example = "miPassword123")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "RUT chileno con formato", example = "12.345.678-9")
    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^(\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK])$", message = "El RUT debe tener el formato XX.XXX.XXX-X (ej: 12.345.678-9)")
    private String rut;

    @Schema(description = "Teléfono", example = "+56912345678")
    private String telefono;

    @Schema(description = "Dirección", example = "Av. Providencia 1234")
    private String direccion;

    @Schema(description = "Comuna", example = "Providencia")
    private String comuna;
}
