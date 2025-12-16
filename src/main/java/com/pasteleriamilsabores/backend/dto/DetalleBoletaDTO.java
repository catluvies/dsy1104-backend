package com.pasteleriamilsabores.backend.dto;

import com.pasteleriamilsabores.backend.model.enums.UnidadMedida;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Detalle de un producto en la boleta")
public class DetalleBoletaDTO {

    @Schema(description = "ID del detalle", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Schema(description = "Nombre del producto", accessMode = Schema.AccessMode.READ_ONLY)
    private String productoNombre;

    @Schema(description = "ID de la variante (opcional)", example = "1")
    private Long varianteId;

    @Schema(description = "Nombre de la variante (generado)", example = "Para 15 personas", accessMode = Schema.AccessMode.READ_ONLY)
    private String varianteNombre;

    @Schema(description = "Cantidad de la variante", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer varianteCantidad;

    @Schema(description = "Unidad de medida de la variante", example = "PORCION", accessMode = Schema.AccessMode.READ_ONLY)
    private UnidadMedida varianteUnidadMedida;

    @Schema(description = "Cantidad comprada", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario al momento de la compra", example = "25000")
    private Double precioUnitario;

    @Schema(description = "Subtotal (cantidad x precio)", accessMode = Schema.AccessMode.READ_ONLY)
    private Double subtotal;
}
