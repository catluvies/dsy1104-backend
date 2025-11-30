package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalle de un producto en la boleta")
public class DetalleBoletaDTO {

    @Schema(description = "ID del detalle", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Schema(description = "Nombre del producto", accessMode = Schema.AccessMode.READ_ONLY)
    private String productoNombre;

    @Schema(description = "Cantidad", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario al momento de la compra", example = "25000")
    private Double precioUnitario;

    @Schema(description = "Subtotal (cantidad x precio)", accessMode = Schema.AccessMode.READ_ONLY)
    private Double subtotal;
}
