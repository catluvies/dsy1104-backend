package com.pasteleriamilsabores.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private Long categoriaId;
    private String categoriaNombre;
    private Double precio;
    private Integer stock;
    private String ingredientes;
    private String formatoVenta;
    private Integer porciones;
    private String peso;
    private String duracion;
    private String tiempoPreparacion;
    private String opcionPersonalizacion;
    private String notas;
    private Boolean activo;
}
