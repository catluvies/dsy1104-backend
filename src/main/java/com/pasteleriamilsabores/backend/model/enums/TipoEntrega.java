package com.pasteleriamilsabores.backend.model.enums;

public enum TipoEntrega {
    RETIRO("Retiro en tienda"),
    DELIVERY("Despacho a domicilio");

    private final String descripcion;

    TipoEntrega(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
