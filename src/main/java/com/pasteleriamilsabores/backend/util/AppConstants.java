package com.pasteleriamilsabores.backend.util;

public final class AppConstants {

    private AppConstants() {
        throw new UnsupportedOperationException("Clase de utilidades");
    }

    public static final String REGION_OPERACION = "Región Metropolitana";

    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_CONFIRMADA = "CONFIRMADA";
    public static final String ESTADO_PREPARANDO = "PREPARANDO";
    public static final String ESTADO_LISTA = "LISTA";
    public static final String ESTADO_ENTREGADA = "ENTREGADA";
    public static final String ESTADO_CANCELADA = "CANCELADA";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_VENDEDOR = "ROLE_VENDEDOR";
    public static final String ROLE_CLIENTE = "ROLE_CLIENTE";
}
