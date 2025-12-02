package com.pasteleriamilsabores.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class CrearBoletaRequest {

    @NotEmpty(message = "La boleta debe tener al menos un producto")
    private List<DetalleBoletaRequest> detalles;

    @NotBlank(message = "La dirección de entrega es obligatoria")
    private String direccionEntrega;

    private String comuna;
    // region no se incluye porque siempre es "Región Metropolitana"
    private Double costoEnvio;

    private com.pasteleriamilsabores.backend.model.enums.TipoEntrega tipoEntrega;
    private com.pasteleriamilsabores.backend.model.enums.HorarioEntrega horarioEntrega;
    private com.pasteleriamilsabores.backend.model.enums.MetodoPago metodoPago;
    private String notasAdicionales;

    private java.time.LocalDateTime fechaEntrega;
}
