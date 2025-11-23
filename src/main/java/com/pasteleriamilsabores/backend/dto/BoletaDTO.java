package com.pasteleriamilsabores.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoletaDTO {

    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private LocalDateTime fecha;
    private Double total;
    private Double subtotal;
    private Double costoEnvio;
    private String metodoPago;
    private String direccionEntrega;
    private String notasAdicionales;
    private String estado;
    private List<DetalleBoletaDTO> detalles;
}
