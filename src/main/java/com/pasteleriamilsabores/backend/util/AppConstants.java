package com.pasteleriamilsabores.backend.util;

public final class AppConstants {

    private AppConstants() {
        throw new UnsupportedOperationException("Clase de utilidades");
    }

    public static final String REGION_OPERACION = "Región Metropolitana";

    public static final int TIEMPO_PAGO_MINUTOS = 120; // 2 horas para transferencia

    public static final String COMUNA_RETIRO = "RETIRO EN TIENDA";
    public static final String DIRECCION_TIENDA = "Av. Providencia 1234, Local 10 (Pastelería Mil Sabores)";

    // Mapa de Costos de Envío por Comuna
    public static final java.util.Map<String, Double> COSTOS_ENVIO = java.util.Map.ofEntries(
            // Zona Centro ($3.000)
            java.util.Map.entry("Santiago", 3000.0),
            java.util.Map.entry("Providencia", 3000.0),
            java.util.Map.entry("Ñuñoa", 3000.0),
            java.util.Map.entry("San Miguel", 3000.0),

            // Zona Oriente ($4.000)
            java.util.Map.entry("Las Condes", 4000.0),
            java.util.Map.entry("Vitacura", 4000.0),
            java.util.Map.entry("La Reina", 4000.0),
            java.util.Map.entry("Peñalolén", 4000.0),

            // Zona Poniente ($3.500)
            java.util.Map.entry("Estación Central", 3500.0),
            java.util.Map.entry("Maipú", 3500.0),
            java.util.Map.entry("Pudahuel", 3500.0),

            // Otras ($5.000)
            java.util.Map.entry("La Florida", 5000.0),
            java.util.Map.entry("Macul", 5000.0),
            java.util.Map.entry("Recoleta", 5000.0),
            java.util.Map.entry("Independencia", 5000.0));
}
