package com.pasteleriamilsabores.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleBoletaRequest {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    // Opcional: si se especifica, se usa el precio y stock de la variante
    private Long varianteId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    private Double precioUnitario;
}
