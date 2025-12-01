package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Datos para cambiar contraseña")
public class CambiarPasswordRequest {

    @Schema(description = "Contraseña actual", example = "miClaveActual")
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String passwordActual;

    @Schema(description = "Nueva contraseña (mínimo 6 caracteres)", example = "miClaveNueva123")
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String passwordNueva;
}
