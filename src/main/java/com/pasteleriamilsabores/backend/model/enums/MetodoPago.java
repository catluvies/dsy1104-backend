package com.pasteleriamilsabores.backend.model.enums;

public enum MetodoPago {
    TRANSFERENCIA("Transferencia bancaria"),
    EFECTIVO("Efectivo contra entrega");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
