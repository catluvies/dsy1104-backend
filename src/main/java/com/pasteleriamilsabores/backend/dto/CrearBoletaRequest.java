package com.pasteleriamilsabores.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CrearBoletaRequest {

    @NotEmpty(message = "La boleta debe tener al menos un producto")
    private List<DetalleBoletaRequest> detalles;

    private String direccionEntrega;
    private Double costoEnvio;
    private String metodoPago;
    private String notasAdicionales;
}
