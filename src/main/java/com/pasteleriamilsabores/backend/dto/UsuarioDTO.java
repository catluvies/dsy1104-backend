package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Datos de un usuario")
public class UsuarioDTO {

    @Schema(description = "ID del usuario", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido", example = "Pérez")
    private String apellido;

    @Schema(description = "Correo electrónico", example = "juan@email.com")
    private String email;

    @Schema(description = "Rol del usuario", example = "ROLE_CLIENTE")
    private String rol;

    @Schema(description = "RUT chileno", example = "12345678-9")
    private String rut;

    @Schema(description = "Teléfono", example = "+56912345678")
    private String telefono;

    @Schema(description = "Dirección", example = "Av. Providencia 1234")
    private String direccion;

    @Schema(description = "Comuna", example = "Providencia")
    private String comuna;

    @Schema(description = "Región", example = "Región Metropolitana")
    private String region;

    @Schema(description = "Si el usuario está activo", example = "true")
    private Boolean activo;

    @Schema(description = "Cantidad de compras realizadas", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer historialCompras;
}
