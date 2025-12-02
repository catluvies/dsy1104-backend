package com.pasteleriamilsabores.backend.model.enums;

public enum CondicionConservacion {
    REFRIGERADO("Mantener refrigerado"),
    CONGELADO("Mantener congelado"),
    AMBIENTE("Temperatura ambiente");

    private final String descripcion;

    CondicionConservacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
