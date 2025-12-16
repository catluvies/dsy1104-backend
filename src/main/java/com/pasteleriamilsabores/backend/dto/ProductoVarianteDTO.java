package com.pasteleriamilsabores.backend.dto;

import com.pasteleriamilsabores.backend.model.enums.UnidadMedida;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de una variante/tamaño de producto")
public class ProductoVarianteDTO {

    @Schema(description = "ID de la variante", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del producto al que pertenece", example = "1")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Debe tener al menos 1")
    @Schema(description = "Cantidad (ej: 12 para 12 personas, 750 para 750ml)", example = "12")
    private Integer cantidad;

    @NotNull(message = "La unidad de medida es obligatoria")
    @Schema(description = "Unidad de medida", example = "PORCION")
    private UnidadMedida unidadMedida;

    @Schema(description = "Nombre generado para mostrar (solo lectura)", example = "Para 12 personas", accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreDisplay;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    @Schema(description = "Precio de esta variante", example = "15000")
    private Double precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Stock disponible", example = "10")
    private Integer stock;

    @Schema(description = "Variante activa o inactiva", example = "true")
    private Boolean activo;
}
