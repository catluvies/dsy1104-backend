package com.pasteleriamilsabores.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String rut;
    private String telefono;
    private String direccion;
    private String comuna;
    private LocalDateTime ultimaConexion;
    private Integer historialCompras;
}
