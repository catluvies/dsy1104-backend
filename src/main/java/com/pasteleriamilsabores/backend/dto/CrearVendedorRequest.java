package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para crear un vendedor (Solo Admin)")
public class CrearVendedorRequest {

    @Schema(description = "Nombre", example = "Maria")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido", example = "Gonzalez")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(description = "Correo corporativo", example = "ventas@pasteleriamilsabores.cl")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @Schema(description = "Contraseña", example = "passwordVentas2024")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "RUT", example = "15.678.901-2")
    @NotBlank(message = "El RUT es obligatorio")
    @Pattern(regexp = "^(\\d{1,2}\\.\\d{3}\\.\\d{3}-[\\dkK])$", message = "El RUT debe tener el formato XX.XXX.XXX-X")
    private String rut;

    @Schema(description = "Teléfono", example = "+56987654321")
    private String telefono;

    @Schema(description = "Dirección", example = "Calle Los Alerces 456")
    private String direccion;

    @Schema(description = "Comuna", example = "Providencia")
    private String comuna;
}
