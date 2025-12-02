package com.pasteleriamilsabores.backend.controller;

import com.pasteleriamilsabores.backend.dto.ComunaDTO;
import com.pasteleriamilsabores.backend.model.enums.*;
import com.pasteleriamilsabores.backend.util.AppConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/configuracion")
@Tag(name = "Configuración", description = "Endpoints para obtener configuraciones del sistema (comunas, enums, etc.)")
public class ConfiguracionController {

    @Operation(summary = "Obtener comunas y costos de envío", description = "Retorna la lista de comunas disponibles y sus costos de envío")
    @GetMapping("/comunas")
    public ResponseEntity<List<ComunaDTO>> getComunas() {
        List<ComunaDTO> comunas = AppConstants.COSTOS_ENVIO.entrySet().stream()
                .map(entry -> new ComunaDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(comunas);
    }

    @Operation(summary = "Obtener tipos de entrega", description = "Retorna los tipos de entrega disponibles")
    @GetMapping("/tipos-entrega")
    public ResponseEntity<List<Map<String, String>>> getTiposEntrega() {
        List<Map<String, String>> tipos = Arrays.stream(TipoEntrega.values())
                .map(t -> Map.of("codigo", t.name(), "descripcion", t.getDescripcion()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(tipos);
    }

    @Operation(summary = "Obtener horarios de entrega", description = "Retorna los horarios de entrega disponibles")
    @GetMapping("/horarios-entrega")
    public ResponseEntity<List<Map<String, String>>> getHorariosEntrega() {
        List<Map<String, String>> horarios = Arrays.stream(HorarioEntrega.values())
                .map(h -> Map.of("codigo", h.name(), "descripcion", h.getDescripcion()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(horarios);
    }

    @Operation(summary = "Obtener métodos de pago", description = "Retorna los métodos de pago disponibles")
    @GetMapping("/metodos-pago")
    public ResponseEntity<List<Map<String, String>>> getMetodosPago() {
        List<Map<String, String>> metodos = Arrays.stream(MetodoPago.values())
                .map(m -> Map.of("codigo", m.name(), "descripcion", m.getDescripcion()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(metodos);
    }

    @Operation(summary = "Obtener unidades de medida", description = "Retorna las unidades de medida disponibles (Solo Admin)")
    @GetMapping("/unidades-medida")
    public ResponseEntity<List<Map<String, String>>> getUnidadesMedida() {
        List<Map<String, String>> unidades = Arrays.stream(UnidadMedida.values())
                .map(u -> Map.of("codigo", u.name(), "simbolo", u.getSimbolo()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(unidades);
    }

    @Operation(summary = "Obtener condiciones de conservación", description = "Retorna las condiciones de conservación disponibles (Solo Admin)")
    @GetMapping("/condiciones-conservacion")
    public ResponseEntity<List<Map<String, String>>> getCondicionesConservacion() {
        List<Map<String, String>> condiciones = Arrays.stream(CondicionConservacion.values())
                .map(c -> Map.of("codigo", c.name(), "descripcion", c.getDescripcion()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(condiciones);
    }

    @Operation(summary = "Obtener estados de boleta", description = "Retorna los estados de boleta disponibles (Admin y Vendedor)")
    @GetMapping("/estados-boleta")
    public ResponseEntity<List<Map<String, String>>> getEstadosBoleta() {
        List<Map<String, String>> estados = Arrays.stream(EstadoBoleta.values())
                .map(e -> Map.of("codigo", e.name(), "descripcion", e.getDescripcion()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(estados);
    }
}
