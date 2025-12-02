package com.pasteleriamilsabores.backend.model.enums;

public enum UnidadMedida {
    ML("ml"), // Mililitros - para bebidas
    L("L"), // Litros
    G("g"), // Gramos
    KG("kg"), // Kilogramos
    UNIDAD("unidad"), // Unidad individual
    PORCION("porción"), // Porción
    DOCENA("docena"); // Docena (12 unidades)

    private final String simbolo;

    UnidadMedida(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
