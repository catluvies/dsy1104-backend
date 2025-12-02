package com.pasteleriamilsabores.backend.model.enums;

public enum HorarioEntrega {
    H_09_11("09:00 - 11:00"),
    H_11_13("11:00 - 13:00"),
    H_14_16("14:00 - 16:00"),
    H_16_18("16:00 - 18:00"),
    H_18_20("18:00 - 20:00");

    private final String descripcion;

    HorarioEntrega(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
