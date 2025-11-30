package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de un producto de la pastelería")
public class ProductoDTO {

    @Schema(description = "ID del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Código SKU único", example = "TORTA-001")
    private String sku;

    @Schema(description = "Nombre del producto", example = "Torta Selva Negra")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Bizcocho de chocolate con crema y guindas")
    private String descripcion;

    @Schema(description = "URL de la imagen", example = "/uploads/torta-selva-negra.jpg")
    private String imagenUrl;

    @Schema(description = "ID de la categoría", example = "1")
    private Long categoriaId;

    @Schema(description = "Nombre de la categoría", accessMode = Schema.AccessMode.READ_ONLY)
    private String categoriaNombre;

    @Schema(description = "Precio en pesos chilenos", example = "25000")
    private Double precio;

    @Schema(description = "Cantidad disponible", example = "10")
    private Integer stock;

    @Schema(description = "Lista de ingredientes")
    private String ingredientes;

    @Schema(description = "Formato de venta", example = "Unidad")
    private String formatoVenta;

    @Schema(description = "Cantidad de porciones", example = "12")
    private Integer porciones;

    @Schema(description = "Peso del producto", example = "1.5 kg")
    private String peso;

    @Schema(description = "Tiempo de duración", example = "3 días refrigerado")
    private String duracion;

    @Schema(description = "Tiempo de preparación", example = "24 horas")
    private String tiempoPreparacion;

    @Schema(description = "Notas adicionales o alérgenos")
    private String notas;

    @Schema(description = "Si el producto está activo", example = "true")
    private Boolean activo;
}
