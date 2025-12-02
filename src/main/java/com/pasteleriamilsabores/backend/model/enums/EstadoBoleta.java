package com.pasteleriamilsabores.backend.model.enums;

public enum EstadoBoleta {
    PENDIENTE("Pendiente de pago"),
    CONFIRMADA("Pago confirmado"),
    PREPARANDO("En preparación"),
    LISTA("Lista para entrega"),
    ENTREGADA("Entregada"),
    CANCELADA("Cancelada");

    private final String descripcion;

    EstadoBoleta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
