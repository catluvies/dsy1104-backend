package com.pasteleriamilsabores.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComunaDTO {
    private String nombre;
    private Double costoEnvio;
}
