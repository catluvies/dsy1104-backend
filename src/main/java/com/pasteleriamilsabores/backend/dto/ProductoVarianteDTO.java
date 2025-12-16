package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre descriptivo de la variante", example = "Para 15 personas")
    private String nombre;

    @NotNull(message = "Las porciones son obligatorias")
    @Min(value = 1, message = "Debe tener al menos 1 porción")
    @Schema(description = "Cantidad de porciones", example = "15")
    private Integer porciones;

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
