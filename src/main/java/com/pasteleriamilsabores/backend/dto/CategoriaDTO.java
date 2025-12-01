package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de una categoría de productos")
public class CategoriaDTO {

    @Schema(description = "ID de la categoría", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre de la categoría", example = "Tortas")
    private String nombre;

    @Schema(description = "Descripción de la categoría", example = "Tortas para celebraciones")
    private String descripcion;

    @Schema(description = "Si la categoría está activa", example = "true")
    private Boolean activa;
}
