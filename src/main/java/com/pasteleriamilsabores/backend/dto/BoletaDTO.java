package com.pasteleriamilsabores.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos de una boleta de compra")
public class BoletaDTO {

    @Schema(description = "ID de la boleta", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario", example = "1")
    private Long usuarioId;

    @Schema(description = "Nombre del usuario", accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "Fecha de creación", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaCreacion;

    @Schema(description = "Total de la boleta", example = "45000")
    private Double total;

    @Schema(description = "Subtotal sin envío", example = "42000")
    private Double subtotal;

    @Schema(description = "Costo de envío", example = "3000")
    private Double costoEnvio;

    @Schema(description = "Tipo de entrega", example = "DELIVERY")
    private com.pasteleriamilsabores.backend.model.enums.TipoEntrega tipoEntrega;

    @Schema(description = "Horario de entrega", example = "H_09_11")
    private com.pasteleriamilsabores.backend.model.enums.HorarioEntrega horarioEntrega;

    @Schema(description = "Método de pago", example = "TRANSFERENCIA")
    private com.pasteleriamilsabores.backend.model.enums.MetodoPago metodoPago;

    @Schema(description = "Dirección de entrega", example = "Av. Providencia 1234")
    private String direccionEntrega;

    @Schema(description = "Comuna de entrega", example = "Providencia")
    private String comunaEntrega;

    @Schema(description = "Región de entrega", example = "Región Metropolitana")
    private String regionEntrega;

    @Schema(description = "Notas adicionales")
    private String notas;

    @Schema(description = "Fecha de entrega solicitada", example = "2024-12-20T15:00:00")
    private java.time.LocalDateTime fechaEntrega;

    @Schema(description = "Estado de la boleta", example = "PENDIENTE")
    private com.pasteleriamilsabores.backend.model.enums.EstadoBoleta estado;

    @Schema(description = "Detalles de productos")
    private List<DetalleBoletaDTO> detalles;

    @Schema(description = "URL del comprobante de transferencia", accessMode = Schema.AccessMode.READ_ONLY)
    private String comprobanteUrl;

    @Schema(description = "Fecha de subida del comprobante", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fechaComprobante;
}
