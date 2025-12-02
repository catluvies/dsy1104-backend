package com.pasteleriamilsabores.backend.model.enums;

public enum RolUsuario {
    ROLE_ADMIN("Administrador"),
    ROLE_VENDEDOR("Vendedor"),
    ROLE_CLIENTE("Cliente");

    private final String descripcion;

    RolUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
